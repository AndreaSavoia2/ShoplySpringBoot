package com.project.shoply.payload.response;

import com.project.shoply.entity.OrderItem;
import com.project.shoply.entity.enumerated.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderResponse {

    private long orderId;
    private String shippingAddress;
    private OrderStatus status;
    private List<OrderItemResponse> orderItems;

    public OrderResponse(long orderId, String shippingAddress, OrderStatus status, List<OrderItemResponse> orderItems) {
        this.orderId = orderId;
        this.shippingAddress = shippingAddress;
        this.status = status;
        this.orderItems = orderItems;
    }
}
