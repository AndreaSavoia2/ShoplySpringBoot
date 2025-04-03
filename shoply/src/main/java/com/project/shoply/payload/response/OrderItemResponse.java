package com.project.shoply.payload.response;

import com.project.shoply.entity.Product;
import lombok.Builder;
import lombok.Data;


import java.math.BigDecimal;

@Data
@Builder
public class OrderItemResponse {

    private long orderItemId;
    private Integer quantity;
    private BigDecimal price;
    private Product product;

    public OrderItemResponse(long orderItemId, Integer quantity, BigDecimal price, Product product) {
        this.orderItemId = orderItemId;
        this.quantity = quantity;
        this.price = price;
        this.product = product;
    }
}
