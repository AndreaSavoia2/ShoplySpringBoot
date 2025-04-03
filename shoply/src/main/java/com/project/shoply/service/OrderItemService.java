package com.project.shoply.service;

import com.project.shoply.entity.Order;
import com.project.shoply.entity.OrderItem;
import com.project.shoply.entity.Product;
import com.project.shoply.entity.view.CartView;
import com.project.shoply.exception.GenericException;
import com.project.shoply.exception.ResourceNotFoundException;
import com.project.shoply.repository.OrderItemRepository;
import com.project.shoply.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;
    private final CartItemService cartItemService;

    protected void addItemToOrder(List<CartView> cartItems, Order order) {

        Set<Long> productIds = new HashSet<>();
        for (CartView cartView : cartItems) {
            productIds.add(cartView.getProductId());
        }
        Iterable<Product> productsIterable = productService.findAllProductById(productIds);

        // Crea una mappa per associare ogni ID al relativo prodotto
        Map<Long, Product> productsMap = new HashMap<>();
        for (Product product : productsIterable) {
            productsMap.put(product.getId(), product);
        }

        List<OrderItem> orderItems = new ArrayList<>();
        List<CartView> orderedProducts = new ArrayList<>();

        for (CartView cartView : cartItems) {

            Product product = productsMap.get(cartView.getProductId());

            if (product == null)
                throw new ResourceNotFoundException("Product", "id", cartView.getProductId());

            if (product.getStock() < cartView.getQuantity())
                throw new GenericException("There are not enough products in stock", HttpStatus.CONFLICT);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartView.getQuantity())
                    .price(product.getPrice())
                    .build();
            orderItems.add(orderItem);

            orderedProducts.add(new CartView(cartView.getProductId(), cartView.getQuantity()));
        }

        orderItemRepository.saveAll(orderItems);
        cartItemService.deleteAllByUserId(order.getUser().getId());
        productService.reduceProductStock(orderedProducts);
    }

    @Transactional
    protected void deleteOrderItemsByOrderId(long orderId){
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(orderId);
        productService.addProductStock(orderItems);
        orderItemRepository.deleteAll(orderItems);
    }
}