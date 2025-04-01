package com.project.shoply.service;

import com.project.shoply.entity.Cart;
import com.project.shoply.entity.CartItem;
import com.project.shoply.entity.Product;
import com.project.shoply.entity.User;
import com.project.shoply.exception.GenericException;
import com.project.shoply.exception.ResourceNotFoundException;
import com.project.shoply.payload.request.AddItemCartRequest;
import com.project.shoply.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartService cartService;
    private final ProductService productService;

    public String addItemToCart(AddItemCartRequest addItemCartRequest, UserDetails userDetails) {
        User user = (User) userDetails;
        long userId = user.getId();
        long cartId = addItemCartRequest.getCartId();
        long productId = addItemCartRequest.getProductId();

        Cart cart = cartService.findCartById(cartId);
        if (cart.getUser().getId() != userId)
            throw new GenericException(
                    "Cart ownership conflict: the cart does not belong to the specified user.",
                    HttpStatus.FORBIDDEN);
        /*if (cartItemRepository.existsByProductIdAndCartId(productId, cartId))
            quantity++;*/

        Product product = productService.findProductById(productId);
        if (!product.isActive())
            throw new DisabledException("Product is not active");

        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(addItemCartRequest.getQuantity())
                .build();

        cartItemRepository.save(cartItem);
        return "Successfully added item to the cart";
    }

    @Transactional
    public String removeItemToCart(UserDetails userDetails) {
        User user = (User) userDetails;
        long userId = user.getId();
        Cart cart = cartService.findCartById(userId);
        CartItem cartItem = findCartItemById(cart.getId());
        cartItemRepository.delete(cartItem);
        return "Product removed to cart";
    }

    protected CartItem findCartItemById(long id) {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item", "id", id));
    }

    protected void deleteAllByUserId(long userId) {
        cartItemRepository.deleteAllByCartUserId(userId);
    }
}
