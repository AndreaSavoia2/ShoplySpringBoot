package com.project.shoply.payload.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductResponse {

    private long id;
    private String Name;
    private BigDecimal price;
    private String category;
    private String brands;
    private String image_url;
    private Integer stock;

}
