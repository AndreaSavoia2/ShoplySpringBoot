package com.project.shoply.payload.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ReviewRequest {

    @Min(1)
    @Max(5)
    private Integer rating;
    @Size(max = 65535)
    private String comment;
    @Min(1)
    private Long productId;
}
