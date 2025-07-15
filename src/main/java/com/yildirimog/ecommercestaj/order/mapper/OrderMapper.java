package com.yildirimog.ecommercestaj.order.mapper;

import com.yildirimog.ecommercestaj.common.enums.OrderStatus;
import com.yildirimog.ecommercestaj.order.dto.OrderItemResponse;
import com.yildirimog.ecommercestaj.order.dto.OrderRequest;
import com.yildirimog.ecommercestaj.order.dto.OrderResponse;
import com.yildirimog.ecommercestaj.order.entity.Order;
import com.yildirimog.ecommercestaj.order.entity.OrderItem;
import com.yildirimog.ecommercestaj.product.entity.Product;
import com.yildirimog.ecommercestaj.user.entity.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrderMapper {
    public OrderResponse toOrderResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(this::toOrderItemResponse)
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                items,
                order.getStatus().name(),
                order.getTotalAmount(),
                order.getCreatedAt()
        );
    }

    public OrderItemResponse toOrderItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getUnitPrice()
        );
    }

    public Order toOrder(OrderRequest request, User user, List<Product> products) {
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = request.orderItems().stream()
                .map(itemReq -> {
                    Product product = products.stream()
                            .filter(p -> p.getId().equals(itemReq.productId()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Ürün bulunamadı: " + itemReq.productId()));

                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(product);
                    orderItem.setQuantity(itemReq.quantity());
                    orderItem.setUnitPrice(product.getPrice());
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .toList();

        order.setItems(orderItems);
        order.setTotalAmount(
                orderItems.stream()
                        .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );

        return order;
    }

    public List<OrderResponse> toResponseList(List<Order> orders) {
        return orders.stream()
                .map(this::toOrderResponse)
                .toList();
    }
}
