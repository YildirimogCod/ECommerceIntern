package com.yildirimog.ecommercestaj.order.service;

import com.yildirimog.ecommercestaj.common.enums.OrderStatus;
import com.yildirimog.ecommercestaj.idempotency.service.IdempotencyService;
import com.yildirimog.ecommercestaj.order.dto.OrderRequest;
import com.yildirimog.ecommercestaj.order.dto.OrderResponse;
import com.yildirimog.ecommercestaj.order.entity.Order;
import com.yildirimog.ecommercestaj.order.mapper.OrderMapper;
import com.yildirimog.ecommercestaj.order.repository.OrderRepository;
import com.yildirimog.ecommercestaj.product.entity.Product;
import com.yildirimog.ecommercestaj.product.repository.ProductRepository;
import com.yildirimog.ecommercestaj.user.entity.User;
import com.yildirimog.ecommercestaj.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final IdempotencyService idempotencyService;

    @Transactional
    @Retryable(
            value = {ObjectOptimisticLockingFailureException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public OrderResponse create(OrderRequest request,String idempotencyKey){
        Optional<OrderResponse> cached = idempotencyService.checkAndGet(
                idempotencyKey,
                request.userId(),
                OrderResponse.class
        );
        if (cached.isPresent()) {
            return cached.get();
        }
       User user = userRepository.findById(request.userId())
               .orElseThrow(()->new RuntimeException("User not found with id: "));
       List<Long> productIds =  request.orderItems().stream()
               .map(orderItemRequest -> orderItemRequest.productId())
               .toList();
       List<Product> products = productRepository.findAllById(productIds);
         if(products.size() != productIds.size()){
              throw new RuntimeException("Some products not found");
         }
        for (Product product:products){
            int requestedQuantity = request.orderItems().stream()
                    .filter(orderItemRequest -> orderItemRequest.productId().equals(product.getId()))
                    .findFirst()
                    .map(orderItemRequest -> orderItemRequest.quantity())
                    .orElse(0);
            if(product.getStockQuantity()< requestedQuantity){
                throw new RuntimeException("Product " + product.getName() + " is out of stock");
            }
            // Reduce stock quantity
            product.setStockQuantity(product.getStockQuantity() - requestedQuantity);
        }
          Order order = orderMapper.toOrder(request, user, products);
          Order savedOrder = orderRepository.save(order);

          idempotencyService.save(idempotencyKey,user.getId(),orderMapper.toOrderResponse(savedOrder)
          );
        System.out.println("Order created with id: " + savedOrder.getId());
         return orderMapper.toOrderResponse(savedOrder);
   }
   public OrderResponse getOrder(Long id){
        Optional<Order> order = orderRepository.findById(id);
        if(order.isEmpty()){
            throw new RuntimeException("Order not found");
        }
        return orderMapper.toOrderResponse(order.get());
   }
   public List<OrderResponse> getOrdersByUser(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(orderMapper::toOrderResponse)
                .toList();
   }
   @Transactional
    public OrderResponse cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Sipariş bulunamadı"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Bu sipariş iptal edilemez. Mevcut durum: " + order.getStatus());
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order updated = orderRepository.save(order);
        return orderMapper.toOrderResponse(updated);
    }
}
