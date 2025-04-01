package com.project.shoply.payload.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderRequest {

    @NotBlank
    private String street;
    @NotNull
    @Min(value = 10000)
    @Max(value = 99999)
    private Integer postalCode;
    @NotBlank
    private String city;
    @NotBlank
    private String region;
    @NotBlank
    private String country;

    public String getFullAddress() {
        return String.format("%s, %d, %s, %s, %s",
                street.trim().toLowerCase(),
                postalCode,
                city.trim().toLowerCase(),
                region.trim().toLowerCase(),
                country.trim().toLowerCase());
    }
}
