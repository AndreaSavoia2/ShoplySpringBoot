package com.project.shoply.service;

import com.project.shoply.entity.view.CartView;
import com.project.shoply.repository.CartViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartViewService {

    private final CartViewRepository cartViewRepository;

    protected List<CartView> findAllByCartId(long cartId) {
        return cartViewRepository.findAllByCartId(cartId);
    }

    protected Integer countByUserId(long userId) {
        return cartViewRepository.countByUserId(userId);
    }

    protected BigDecimal getTotalCartValue(long userId) {
        return cartViewRepository.getTotalCartValue(userId);
    }
}
