package com.project.shoply.payload.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class AddItemCartRequest {

    @Min(1)
    private long cartId;
    @Min(1)
    private long productId;
    @Min(1)
    private int quantity;
}
