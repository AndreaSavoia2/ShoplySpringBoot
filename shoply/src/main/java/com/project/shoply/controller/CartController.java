package com.project.shoply.controller;


import com.project.shoply.payload.request.AddItemCartRequest;
import com.project.shoply.service.CartItemService;
import com.project.shoply.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;

    @PostMapping("v1/carts")
    @Operation(
            summary = "Aggiunge un articolo al carrello",
            description = "Aggiunge un prodotto specificato al carrello dell'utente autenticato."
    )
    public ResponseEntity<?> addItemToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid AddItemCartRequest addItemCartRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemService.addItemToCart(addItemCartRequest, userDetails));
    }

    @GetMapping("v1/carts")
    @Operation(
            summary = "Recupera il carrello dell'utente",
            description = "Restituisce il carrello associato all'utente autenticato."
    )
    public ResponseEntity<?> getCartByCartId(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(cartService.findAllByUserId(userDetails));
    }

    @GetMapping("v1/carts/count-item")
    @Operation(
            summary = "Conta gli articoli nel carrello",
            description = "Restituisce il numero totale di articoli presenti nel carrello dell'utente autenticato."
    )
    public ResponseEntity<?> countByCartId(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(cartService.countByUserId(userDetails));
    }

    @GetMapping("v1/carts/sum-subtotal-item")
    @Operation(
            summary = "Calcola il valore totale del carrello",
            description = "Restituisce il valore totale del carrello dell'utente autenticato, calcolando il subtotale degli articoli presenti."
    )
    public ResponseEntity<?> getTotalCartValue(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(cartService.getTotalCartValue(userDetails));
    }

    @DeleteMapping("v1/carts")
    @Operation(
            summary = "Rimuove un articolo dal carrello",
            description = "Rimuove un articolo dal carrello dell'utente autenticato."
    )
    public ResponseEntity<?> removeItemToCart(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(cartItemService.removeItemToCart(userDetails));
    }
}
