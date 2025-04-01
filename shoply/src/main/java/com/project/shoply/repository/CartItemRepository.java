package com.project.shoply.repository;

import com.project.shoply.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByProductId(long productId);

    void deleteAllByCartUserId(long userId);
}
