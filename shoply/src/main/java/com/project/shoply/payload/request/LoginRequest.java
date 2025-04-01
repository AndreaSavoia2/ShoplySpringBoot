package com.project.shoply.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginRequest {

    @NotBlank
    @Size(max = 20, min = 4)
    private String username;

    @NotBlank
    private String password;

}
