package com.project.shoply.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RegistrationRequest {

    @NotBlank
    @Size(min = 2, max = 50)
    private String username;
    @NotBlank
    private String password;
    @Email
    @NotBlank
    private String email;
}
