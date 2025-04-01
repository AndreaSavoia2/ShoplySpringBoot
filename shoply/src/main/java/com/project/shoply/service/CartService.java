package com.project.shoply.service;

import com.project.shoply.entity.Cart;
import com.project.shoply.entity.User;
import com.project.shoply.entity.view.CartView;
import com.project.shoply.exception.ResourceNotFoundException;
import com.project.shoply.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartViewService cartViewService;

    public List<CartView> findAllByUserId(UserDetails userDetails) {
        User user = (User) userDetails;
        long userId = user.getId();
        Cart cart = findCartByUserId(userId);
        return cartViewService.findAllByCartId(cart.getId());
    }

    public Integer countByUserId(UserDetails userDetails) {
        User user = (User) userDetails;
        long userId = user.getId();
        return cartViewService.countByUserId(userId);
    }

    public BigDecimal getTotalCartValue(UserDetails userDetails) {
        User user = (User) userDetails;
        long userId = user.getId();
        return cartViewService.getTotalCartValue(userId);
    }

    protected void createCartForUser(User user) {
        Cart cart = new Cart(user);
        cartRepository.save(cart);
    }

    protected Cart findCartById(long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", id));
    }

    protected Cart findCartByUserId(long id) {
        return cartRepository.findByUserId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "user id", id));
    }

}
