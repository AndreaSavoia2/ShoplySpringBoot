package com.project.shoply.controller;

import com.project.shoply.service.CategoryService;
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

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("v1/categories")
    @PreAuthorize("hasAnyAuthority('OPERATOR')")
    @Operation(
            summary = "Salva una nuova categoria nel database",
            description = "Salva una nuova categoria nel database, la categoria non può avere lo stesso nome di un'altra categoria presente sul database.",
            parameters = @Parameter(
                    name = "categoryName",
                    description = "Il nome della categoria da salvare",
                    required = true,
                    example = "Salone"
            )
    )
    public ResponseEntity<?> saveCategory(@RequestParam @NotBlank @Size(max = 100) String categoryName) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.saveCategory(categoryName));
    }

    @PutMapping("v1/categories")
    @PreAuthorize("hasAnyAuthority('OPERATOR')")
    @Operation(
            summary = "Modifica il nome della categoria",
            description = "Modifica il nome della categoria, il nuovo nome non deve essere già presente nel database.",
            parameters = {
                    @Parameter(
                            name = "newCategoryName",
                            description = "Il nuovo nome della categoria",
                            required = true,
                            example = "Salone"
                    ),
                    @Parameter(
                            name = "categoryId",
                            description = "Id della categoria da modificare",
                            required = true,
                            example = "123"
                    )
            }
    )
    public ResponseEntity<?> updateCategory(
            @RequestParam @NotBlank @Size(max = 100) String newCategoryName,
            @RequestParam @Min(1) long categoryId
    ) {
        return ResponseEntity.ok(categoryService.updateCategory(newCategoryName,categoryId));
    }

    @GetMapping("v1/categories")
    @PreAuthorize("hasAnyAuthority('OPERATOR')")
    @Operation(
            summary = "Lista delle categorie",
            description = "Resistuisce la lista delle categorie presenti nel database, in caso non ce ne siano restituisce []."
    )
    public ResponseEntity<?> getAllCategory() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
