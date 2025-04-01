package com.project.shoply.payload.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class WishlistItemResponse {

    private long productId;
    private String productName;
    private BigDecimal productPrice;
    private String productImageUrl;
}
