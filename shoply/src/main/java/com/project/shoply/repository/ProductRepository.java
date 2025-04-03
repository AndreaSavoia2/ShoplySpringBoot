package com.project.shoply.repository;

import com.project.shoply.entity.Product;
import com.project.shoply.payload.response.ProductDetailsResponse;
import com.project.shoply.payload.response.ProductResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT new com.project.shoply.payload.response.ProductResponse( " +
            "p.id, " +
            "p.name," +
            "p.price," +
            "p.category.name," +
            "p.brand.name," +
            "p.imageUrl," +
            "p.stock" +
            ") FROM Product p WHERE p.active = true")
    List<ProductResponse> getAllProducts();

    @Query("SELECT new com.project.shoply.payload.response.ProductDetailsResponse( " +
            "p.id," +
            "p.name," +
            "p.description," +
            "p.price," +
            "p.material," +
            "p.color," +
            "p.width," +
            "p.height," +
            "p.depth," +
            "p.stock," +
            "p.imageUrl," +
            "p.category.name," +
            "p.brand.name) " +
            "FROM Product p " +
            "WHERE p.id = :productId AND p.active = true")
    ProductDetailsResponse getProductDetailsByProductId(Long productId);

    @Query("SELECT new com.project.shoply.payload.response.ProductResponse( " +
            "p.id, " +
            "p.name," +
            "p.price," +
            "p.category.name," +
            "p.brand.name," +
            "p.imageUrl," +
            "p.stock" +
            ") FROM Product p " +
            "WHERE p.category.name = :category AND p.active = true")
    List<ProductResponse> getAllProductsByCategory(String category);

    @Query("SELECT new com.project.shoply.payload.response.ProductResponse( " +
            "p.id, " +
            "p.name," +
            "p.price," +
            "p.category.name," +
            "p.brand.name," +
            "p.imageUrl," +
            "p.stock" +
            ") FROM Product p " +
            "WHERE p.brand.name = :brand AND p.active = true")
    List<ProductResponse> getAllProductsByBrand(String brand);

    // -------------------- v2 findbar-------------------

    @Query("SELECT new com.project.shoply.payload.response.ProductResponse( " +
            "p.id, " +
            "p.name," +
            "p.price," +
            "p.category.name," +
            "p.brand.name," +
            "p.imageUrl," +
            "p.stock" +
            ") FROM Product p " +
            "WHERE LOWER(p.category.name) LIKE LOWER(CONCAT('%', :category, '%')) " +
            "AND p.active = true")
    List<ProductResponse> getAllProductsByCategoryV2(String category);

    @Query("SELECT new com.project.shoply.payload.response.ProductResponse( " +
            "p.id, " +
            "p.name," +
            "p.price," +
            "p.category.name," +
            "p.brand.name," +
            "p.imageUrl," +
            "p.stock" +
            ") FROM Product p " +
            "WHERE LOWER(p.brand.name) LIKE LOWER(CONCAT('%', :brand, '%'))" +
            "AND p.active = true")
    List<ProductResponse> getAllProductsByBrandV2(String brand);

    // -------------------- v2 findbar-------------------

    @Query("SELECT COALESCE(TRUNCATE(AVG(r.rating), 0), 0) " +
            "FROM Product p " +
            "JOIN Review r ON r.product.id = p.id " +
            "WHERE p.id = :productId AND r.enable = true")
    Integer getRatingProduct(Long productId);

    @Modifying
    @Query("UPDATE Product p SET p.stock = p.stock - :quantity WHERE p.id = :productId AND p.stock >= :quantity")
    int reduceStock(Long productId, int quantity);

    @Modifying
    @Query("UPDATE Product p SET p.stock = p.stock + :quantity WHERE p.id = :productId AND p.stock >= :quantity")
    int addStock(Long productId, int quantity);

    @Modifying
    @Query("UPDATE Product p SET p.stock = p.stock - :quantity WHERE p.id IN :productIds AND p.stock >= :quantity")
    int reduceStockBulk( List<Long> productIds,  int quantity);

    @Modifying
    @Query("UPDATE Product p SET p.stock = p.stock + :quantity WHERE p.id IN :productIds")
    int addStockBulk( List<Long> productIds, int quantity);

}
