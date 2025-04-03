package com.project.shoply.controller;

import com.project.shoply.payload.request.OrderRequest;
import com.project.shoply.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("v1/orders")
    public ResponseEntity<?> createOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid OrderRequest orderRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(userDetails, orderRequest));
    }

    @DeleteMapping("v1/orders/{orderId}")
    public ResponseEntity<?> deleteOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable @Min(1) long orderId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.deleteOrder(userDetails, orderId));
    }
}
