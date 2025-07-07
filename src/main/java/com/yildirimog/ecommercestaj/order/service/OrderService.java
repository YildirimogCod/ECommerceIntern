package com.yildirimog.ecommercestaj.order.service;

import com.yildirimog.ecommercestaj.common.enums.OrderStatus;
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
import org.springframework.stereotype.Service;
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

    public OrderResponse create(@RequestBody OrderRequest request){
       User user = userRepository.findById(request.userId())
               .orElseThrow(()->new RuntimeException("User not found with id: "));
       List<Long> productIds =  request.orderItems().stream()
               .map(orderItemRequest -> orderItemRequest.productId())
               .toList();
       List<Product> products = productRepository.findAllById(productIds);
         if(products.size() != productIds.size()){
              throw new RuntimeException("Some products not found");
         }
        Order order = orderMapper.toOrder(request, user, products);
          Order savedOrder = orderRepository.save(order);
         return orderMapper.toOrderResponse(savedOrder);
   }
   public OrderResponse getOrder(Long id){
        Optional<Order> order = orderRepository.findById(id);
        if(order.isEmpty()){
            throw new RuntimeException("Order not found with id: " + id);
        }
        return orderMapper.toOrderResponse(order.get());
   }
   public List<OrderResponse> getOrdersByUser(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(orderMapper::toOrderResponse)
                .toList();
   }
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
