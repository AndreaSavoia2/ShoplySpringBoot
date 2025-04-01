package com.project.shoply.controller;

import com.project.shoply.service.WishlistItemService;
import com.project.shoply.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;
    private final WishlistItemService wishlistItemService;

    @GetMapping("v1/wishlists")
    @Operation(
            summary = "Recupera le wishlist dell'utente",
            description = "Restituisce tutte le wishlist associate all'utente autenticato."
    )
    public ResponseEntity<?> findAllByUserId(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(wishlistService.findAllByUserId(userDetails));
    }

    @PostMapping("v1/wishlists")
    @Operation(
            summary = "Crea una nuova wishlist",
            description = "Crea una nuova wishlist per l'utente autenticato, specificando il titolo.",
            parameters = {
                    @Parameter(name = "title", description = "Titolo della wishlist (massimo 100 caratteri)", required = true)
            }
    )
    public ResponseEntity<?> CreateWishlist(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam @NotBlank @Size(max = 100) String title
    ) {
        return ResponseEntity.ok(wishlistService.CreateWishlist(title, userDetails));
    }

    @DeleteMapping("v1/wishlists/delete/item/{wishlistId}")
    @Operation(
            summary = "Rimuove un elemento dalla wishlist",
            description = "Rimuove un elemento dalla wishlist specificata dall'ID, per l'utente autenticato.",
            parameters = {
                    @Parameter(name = "wishlistId", description = "ID della wishlist da cui rimuovere l'elemento", required = true)
            }
    )
    public ResponseEntity<?> removeWishlistItem(
            @PathVariable @Min(1) long wishlistId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(wishlistItemService.removeWishlistItem(wishlistId, userDetails));
    }

    @DeleteMapping("v1/wishlists/delete/wishlist/{wishlistId}")
    @Operation(
            summary = "Elimina una wishlist",
            description = "Elimina la wishlist identificata dall'ID per l'utente autenticato.",
            parameters = {
                    @Parameter(name = "wishlistId", description = "ID della wishlist da eliminare", required = true)
            }
    )
    public ResponseEntity<?> deleteWishList(
            @PathVariable @Min(1) long wishlistId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(wishlistService.deleteWishList(userDetails, wishlistId));
    }

    @PostMapping("v1/wishlists/add-item")
    @Operation(
            summary = "Aggiunge un elemento alla wishlist",
            description = "Aggiunge un elemento (prodotto) alla wishlist specificata per l'utente autenticato.",
            parameters = {
                    @Parameter(name = "wishlistId", description = "ID della wishlist a cui aggiungere l'elemento", required = true),
                    @Parameter(name = "productId", description = "ID del prodotto da aggiungere", required = true)
            }
    )
    public ResponseEntity<?> addWishlistItem(
            @RequestParam @Min(1) long wishlistId,
            @RequestParam @Min(1) long productId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(wishlistItemService.addWishlistItem(wishlistId, productId, userDetails));
    }

    @GetMapping("v1/wishlists/see-wishlist-item/{wishlistId}")
    @Operation(
            summary = "Visualizza gli elementi della wishlist",
            description = "Restituisce i dettagli degli elementi presenti nella wishlist identificata dall'ID per l'utente autenticato.",
            parameters = {
                    @Parameter(name = "wishlistId", description = "ID della wishlist di cui visualizzare gli elementi", required = true)
            }
    )
    public ResponseEntity<?> findWishlistItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable @Min(1) long wishlistId
    ) {
        return ResponseEntity.ok(wishlistService.getWishlistDetailsItems(userDetails, wishlistId));
    }

}
