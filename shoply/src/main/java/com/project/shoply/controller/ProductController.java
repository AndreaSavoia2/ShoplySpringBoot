package com.project.shoply.controller;

import com.project.shoply.payload.request.ProductRequest;
import com.project.shoply.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("v0/products")
    @Operation(
            summary = "Recupera tutti i prodotti",
            description = "Restituisce un elenco di tutti i prodotti disponibili nel sistema che non siano stati disattivati."
    )
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("v0/products/{productId}")
    @Operation(
            summary = "Recupera il dettaglio di un prodotto",
            description = "Restituisce i dettagli di un singolo prodotto dato il suo ID. L'ID del prodotto deve essere maggiore di 0.",
            parameters = @Parameter(
                    name = "productId",
                    description = "ID del prodotto da cercare",
                    required = true,
                    example = "123"
            )
    )
    public ResponseEntity<?> getProductById(@PathVariable @Min(1) long productId) {
        return ResponseEntity.ok(productService.getProductDetailsByProductId(productId));
    }

    @GetMapping("v0/products/find-by-categories/{category}")
    @Operation(
            summary = "Recupera prodotti per categoria",
            description = "Restituisce i prodotti attivi appartenenti alla categoria specificata. Se la categoria non esiste, restituisce un elenco vuoto.",
            parameters = @Parameter(
                    name = "category",
                    description = "Nome della categoria di prodotti da cercare",
                    required = true,
                    example = "elettronica"
            )
    )
    public ResponseEntity<?> getAllProductsByCategory(@PathVariable @NotBlank @Size(max = 100) String category) {
        return ResponseEntity.ok(productService.getAllProductsByCategory(category));
    }

    @GetMapping("v0/products/find-by-brands/{brand}")
    @Operation(
            summary = "Recupera prodotti per brand",
            description = "Restituisce i prodotti attivi appartenenti al brand specificata. Se il brand non esiste, restituisce un elenco vuoto.",
            parameters = @Parameter(
                    name = "category",
                    description = "Nome del brand di prodotti da cercare",
                    required = true,
                    example = "sony"
            )
    )
    public ResponseEntity<?> getAllProductsByBrand(@PathVariable @NotBlank @Size(max = 100) String brand) {
        return ResponseEntity.ok(productService.getAllProductsByBrand(brand));
    }

    @PostMapping("v1/products")
    @PreAuthorize("hasAnyAuthority('OPERATOR')")
    @Operation(
            summary = "Aggiungi un nuovo prodotto",
            description = "Aggiungi un nuovo prodotto al sistema. Accessibile solo agli utenti operatori."
    )
    public ResponseEntity<?> addNewProduct(@RequestBody @Valid ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addNewProduct(request));
    }

    @PatchMapping("v1/products/deactivate/{productId}")
    @PreAuthorize("hasAnyAuthority('OPERATOR')")
    @Operation(
            summary = "Disattiva un prodotto",
            description = "Disattiva un prodotto dato il suo ID. Questo endpoint è accessibile solo agli utenti operatori.",
            parameters = @Parameter(
                    name = "productId",
                    description = "ID del prodotto da disattivare",
                    required = true,
                    example = "123"
            )
    )
    public ResponseEntity<?> deactivateProductById(@PathVariable @Min(1) long productId) {
        return ResponseEntity.ok(productService.deactivateProductById(productId));
    }

    @PatchMapping("v1/products/activate/{productId}")
    @PreAuthorize("hasAnyAuthority('OPERATOR')")
    @Operation(
            summary = "Attiva un prodotto",
            description = "Attiva un prodotto dato il suo ID. Questo endpoint è accessibile solo agli operatori.",
            parameters = @Parameter(
                    name = "productId",
                    description = "ID del prodotto da attivare",
                    required = true,
                    example = "123"
            )
    )
    public ResponseEntity<?> activateProductById(@PathVariable @Min(1) long productId) {
        return ResponseEntity.ok(productService.activateProductById(productId));
    }

    @GetMapping("v0/products/rating/{productId}")
    @Operation(
            summary = "Recupera la valutazione di un prodotto",
            description = "Restituisce la valutazione di un prodotto dato il suo ID. L'ID del prodotto deve essere maggiore di 0.",
            parameters = @Parameter(
                    name = "productId",
                    description = "ID del prodotto per ottenere la valutazione",
                    required = true,
                    example = "123"
            )
    )
    public ResponseEntity<?> getRatingProduct(@PathVariable @Min(1) Long productId) {
        return ResponseEntity.ok(productService.getRatingProduct(productId));
    }

    @PatchMapping("v1/products/update-price/{productId}")
    @PreAuthorize("hasAnyAuthority('OPERATOR')")
    @Operation(
            summary = "Modifica il prezzo di un prodotto",
            description = "Modifica il prezzo del prodotto dato il suo ID. L'ID del prodotto deve essere maggiore di 0.",
            parameters = {
                    @Parameter(
                            name = "productId",
                            description = "ID del prodotto per ottenere la valutazione",
                            required = true,
                            example = "123"
                    ),
                    @Parameter(
                            name = "newPrice",
                            description = "Nuovo prezzo da applicare",
                            required = true,
                            example = "49.99"
                    )

            }
    )
    public ResponseEntity<?> updateProductPrice (
            @RequestParam @Digits(integer = 10, fraction = 2) @DecimalMin("0.01") @DecimalMax("9999999999.99") BigDecimal newPrice,
            @PathVariable @Min(1) Long productId
    ) {
        return ResponseEntity.ok(productService.updateProductPrice(productId, newPrice));
    }

}
