package com.project.shoply.payload.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductDetailsResponse {

    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String material;
    private String color;
    private BigDecimal width;
    private BigDecimal height;
    private BigDecimal depth;
    private Integer stock;
    private String imageUrl;
    private String category;
    private String brands;
}
