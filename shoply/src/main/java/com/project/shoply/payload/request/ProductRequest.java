package com.project.shoply.payload.request;

import jakarta.validation.constraints.*;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 65535)
    private String description;

    @Digits(integer = 10, fraction = 2)
    @DecimalMin("0.01")
    @DecimalMax("9999999999.99")
    private BigDecimal price;

    @Size(max = 100)
    private String material;

    @Size(max = 50)
    private String color;

    @Digits(integer = 6, fraction = 2)
    @DecimalMin("0.01")
    @DecimalMax("999999.99")
    private BigDecimal width;

    @Digits(integer = 6, fraction = 2)
    @DecimalMin("0.01")
    @DecimalMax("999999.99")
    private BigDecimal height;

    @Digits(integer = 6, fraction = 2)
    @DecimalMin("0.01")
    @DecimalMax("999999.99")
    private BigDecimal depth;

    @Min(0)
    private Integer stock;

    @Size(max = 50)
    private String imageUrl;

    @Size(max = 100)
    @NotBlank
    private String category;

    @Size(max = 100)
    @NotBlank
    private String brand;

    public String getImageUrl2() {

        return imageUrl.isEmpty() ? null : imageUrl;
    }

    public @Size(max = 65535) String getDescription() {
        return description != null && (description.isEmpty() || description.isBlank()) ? null : description;
    }

    public @Size(max = 100) String getMaterial() {
        return material != null && (material.isEmpty() || material.isBlank()) ? null : material;
    }

    public @Size(max = 50) String getColor() {
        return color != null && (color.isEmpty() || color.isBlank()) ? null : color;
    }

    public @Size(max = 50) String getImageUrl() {
        return imageUrl != null && (imageUrl.isEmpty() || imageUrl.isBlank()) ? null : imageUrl;
    }
}
