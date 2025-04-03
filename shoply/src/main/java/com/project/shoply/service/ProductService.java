package com.project.shoply.service;

import com.project.shoply.entity.*;
import com.project.shoply.entity.view.CartView;
import com.project.shoply.exception.GenericException;
import com.project.shoply.exception.ResourceNotFoundException;
import com.project.shoply.payload.request.ProductRequest;
import com.project.shoply.payload.response.ProductDetailsResponse;
import com.project.shoply.payload.response.ProductResponse;
import com.project.shoply.repository.CartItemRepository;
import com.project.shoply.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final BrandService brandService;

    private final CartItemRepository cartItemRepository;

    public List<ProductResponse> getAllProducts() {
        return productRepository.getAllProducts();
    }

    public ProductDetailsResponse getProductDetailsByProductId(Long productId) {
        long id = findProductById(productId).getId();
        return productRepository.getProductDetailsByProductId(id);
    }

    public List<ProductResponse> getAllProductsByCategory(String category) {
        return productRepository.getAllProductsByCategory(category);
    }

    public List<ProductResponse> getAllProductsByBrand(String brand) {
        return productRepository.getAllProductsByBrand(brand);
    }

    @Transactional
    public String addNewProduct(ProductRequest request) {
        // Cercare se la categoria esiste nel database
        String categoryName = request.getCategory().toLowerCase().trim();
        Category category = categoryService.findCategoryByName(categoryName);

        // Cercare se il Brand esiste nella categoria
        String brandName = request.getBrand().toLowerCase().trim();
        Brand brand = brandService.findBrandByName(brandName);

        // Creazione dell'entità da salvare
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .material(request.getMaterial())
                .color(request.getColor())
                .width(request.getWidth())
                .height(request.getHeight())
                .depth(request.getDepth())
                .stock(request.getStock())
                .imageUrl(request.getImageUrl())
                .category(category)
                .brand(brand)
                .active(true)
                .build();

        // salvataggio dell'entità
        productRepository.save(product);
        return "Product created";
    }

    @Transactional
    public String deactivateProductById(Long productId) {
        Product product = findProductById(productId);
        product.setActive(false);
        List<CartItem> cartItems = cartItemRepository.findAllByProductId(productId);
        cartItemRepository.deleteAll(cartItems);
        return "deactivated";
    }

    @Transactional
    public String activateProductById(Long productId) {
        Product product = findProductById(productId);
        product.setActive(true);
        return "activated";
    }

    @Transactional
    public String updateProductPrice(Long productId, BigDecimal newPrice) {
        Product product = findProductById(productId);
        product.setPrice(newPrice);
        return "updated price";
    }

    @Transactional
    protected void reduceProductStock(List<CartView> cartItems) {
        for (CartView cartItem : cartItems) {
            int updatedRows = productRepository.reduceStock(cartItem.getProductId(), cartItem.getQuantity());
            if (updatedRows == 0) {
                throw new GenericException("There is not enough stock for the product with id: "
                        + cartItem.getProductId(), HttpStatus.CONFLICT);
            }
        }
    }

    @Transactional
    protected void addProductStock(List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            productRepository.addStock(orderItem.getProduct().getId(), orderItem.getQuantity());
        }
    }

    public Integer getRatingProduct(Long productId) {
        return productRepository.getRatingProduct(productId);
    }

    protected boolean existsProductById(Long productId) {
        return productRepository.existsById(productId);
    }

    protected Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "Id", id));
    }

    protected List<Product> findAllProductById(Iterable<Long> productIds) {
        return productRepository.findAllById(productIds);
    }

}
