package com.project.shoply.repository;

import com.project.shoply.entity.view.CartView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface CartViewRepository extends JpaRepository<CartView, Long> {

    List<CartView> findAllByCartId(long userId);

    Integer countByUserId(long cartId);

    @Query("SELECT SUM (cv.subtotal) FROM CartView cv WHERE cv.userId = :userId")
    BigDecimal getTotalCartValue(long userId);

}
