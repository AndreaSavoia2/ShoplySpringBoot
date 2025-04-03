package com.project.shoply.controller;

import com.project.shoply.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class BrandController {

    private final BrandService brandService;

    @PostMapping("v1/brands")
    @PreAuthorize("hasAnyAuthority('OPERATOR')")
    @Operation(
            summary = "Salva un nuovo brand nel database",
            description = "Salva un nuovo brand nel database, il brand non può avere lo stesso nome di un altro brand presente sul database.",
            parameters = @Parameter(
                    name = "brandName",
                    description = "Il nome del brand da salvare",
                    required = true,
                    example = "Sony"
            )
    )
    public ResponseEntity<?> saveBrand(@RequestParam @NotBlank @Size(max = 100) String brandName) {
        return ResponseEntity.status(HttpStatus.CREATED).body(brandService.saveBrand(brandName));
    }

    @PutMapping("v1/brands")
    @PreAuthorize("hasAnyAuthority('OPERATOR')")
    @Operation(
            summary = "Modifica il nome del brand",
            description = "Modifica il nome del brand, il nuovo nome non deve essere già presente nel database.",
            parameters = {
                    @Parameter(
                            name = "newBrandName",
                            description = "Il nuovo nome da dare al brand",
                            required = true,
                            example = "Sony"
                    ),
                    @Parameter(
                            name = "brandId",
                            description = "Id del brand da modificare",
                            required = true,
                            example = "123"
                    )
            }
    )
    public ResponseEntity<?> updateBrand(
            @RequestParam @NotBlank @Size(max = 100) String newBrandName,
            @RequestParam @Min(1) long brandId
    ) {
        return ResponseEntity.ok(brandService.updateBrand(newBrandName, brandId));
    }

    @GetMapping("v0/brands")
    @Operation(
            summary = "Lista dei brand",
            description = "Resistuisce la lista dei brand presenti nel database, in caso non ce ne siano restituisce []."
    )
    public ResponseEntity<?> getAllBrands() {
        return ResponseEntity.ok(brandService.getAllBrands());
    }

}
