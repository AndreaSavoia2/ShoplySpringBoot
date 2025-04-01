package com.project.shoply.controller;

import com.project.shoply.entity.enumerated.ReviewCondition;
import com.project.shoply.payload.request.ReviewRequest;
import com.project.shoply.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("v1/reviews")
    @Operation(
            summary = "Aggiunge una recensione a un prodotto",
            description = "Aggiunge una recensione al prodotto, il rating va da 1 a 5."
    )
    public ResponseEntity<?> createReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid ReviewRequest reviewRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(userDetails, reviewRequest));
    }

    @GetMapping("v0/reviews/{productId}")
    @Operation(
            summary = "Recupera recensione per prodotto",
            description = "Recupera la recensione associata al prodotto identificato da productId.",
            parameters = {
                    @Parameter(name = "productId", description = "ID del prodotto", required = true)
            }
    )
    public ResponseEntity<?> getReviewByProductId(@PathVariable @Min(1) long productId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.getReviewByProductId(productId));
    }

    @PatchMapping("v0/reviews/reporting/{reviewId}")
    @Operation(
            summary = "Reporta una recensione",
            description = "Consente di reportare una recensione specifica identificata da reviewId.",
            parameters = {
                    @Parameter(name = "reviewId", description = "ID della recensione da reportare", required = true)
            }
    )
    public ResponseEntity<?> reportingReview(@PathVariable @Min(1) long reviewId) {
        return ResponseEntity.ok(reviewService.reportingReview(reviewId));
    }

    @PatchMapping("v0/reviews/pending/{reviewId}")
    @PreAuthorize("hasAnyAuthority('OPERATOR')")
    @Operation(
            summary = "Modifica stato recensione",
            description = "Permette ad un operatore di modificare lo stato della recensione (ad esempio, rehabilitated o censured) per la review specificata.",
            parameters = {
                    @Parameter(name = "reviewId", description = "ID della recensione", required = true),
                    @Parameter(name = "reviewCondition", description = "Condizione di revisione da impostare, acetta solo CENSURED o REHABILITATED", required = true)
            }
    )
    public ResponseEntity<?> rehabilitatedOrCensuredReview(
            @PathVariable @Min(1) long reviewId,
            @RequestParam ReviewCondition reviewCondition
    ) {
        return ResponseEntity.ok(reviewService.rehabilitatedOrCensuredReview(reviewCondition, reviewId));
    }

    @GetMapping("v1/reviews/pending")
    @PreAuthorize("hasAnyAuthority('OPERATOR')")
    @Operation(
            summary = "Lista delle recensioni in stato di report (PENDING)",
            description = "Restituisce la lista delel recensioni che sono in stato di PENDING e che dunque hanno subito un report, " +
                    "in caso non siano presenti elementi nel database restituisce [ ]."
    )
    public ResponseEntity<?> getPendingReview() {
        return ResponseEntity.ok(reviewService.getPendingReview());
    }


}
