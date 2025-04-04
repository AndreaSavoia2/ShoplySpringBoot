package com.project.shoply.controller;

import com.project.shoply.payload.request.OrderRequest;
import com.project.shoply.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    @Operation(
            summary = "Crea un nuovo ordine",
            description = "Crea un nuovo ordine per l'utente autenticato utilizzando i dati forniti nella richiesta.",
            parameters = {
                    @Parameter(name = "orderRequest", description = "Dati dell'ordine da creare", required = true)
            }
    )
    public ResponseEntity<?> createOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid OrderRequest orderRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(userDetails, orderRequest));
    }

    @DeleteMapping("v1/orders/{orderId}")
    @Operation(
            summary = "Elimina un ordine",
            description = "Elimina l'ordine identificato dall'ID per l'utente autenticato, l'ordine deve essere in stato di prepared.",
            parameters = {
                    @Parameter(name = "orderId", description = "ID dell'ordine da eliminare", required = true)
            }
    )
    public ResponseEntity<?> deleteOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable @Min(1) long orderId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.deleteOrder(userDetails, orderId));
    }

    @GetMapping("v1/orders")
    @Operation(
            summary = "Recupera tutti gli ordini dell'utente",
            description = "Restituisce tutti gli ordini associati all'utente autenticato."
    )
    public ResponseEntity<?> getAllOrderByUser(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(orderService.getAllOrderByUser(userDetails));
    }

    @GetMapping("v1/orders/{orderId}")
    @Operation(
            summary = "Recupera i dettagli di un ordine",
            description = "Restituisce i dettagli dell'ordine identificato dall'ID per l'utente autenticato.",
            parameters = {
                    @Parameter(name = "orderId", description = "ID dell'ordine di cui si vogliono vedere i dettagli", required = true)
            }
    )
    public ResponseEntity<?> getOrderDetails(
            @PathVariable @Min(1) long orderId,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return ResponseEntity.ok(orderService.getOrderDetails(userDetails, orderId));
    }
}
