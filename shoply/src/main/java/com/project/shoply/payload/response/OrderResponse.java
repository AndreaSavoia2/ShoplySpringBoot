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
}
