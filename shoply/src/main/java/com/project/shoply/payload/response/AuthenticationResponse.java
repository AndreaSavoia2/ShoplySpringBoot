package com.project.shoply.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {
    private long id;
    private String username;
    private long cartId;
    private String token;
}
