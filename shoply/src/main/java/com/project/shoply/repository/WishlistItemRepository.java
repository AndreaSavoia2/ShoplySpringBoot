package com.project.shoply.repository;

import com.project.shoply.entity.WishlistItem;
import com.project.shoply.payload.response.WishlistItemResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {

    boolean existsByProductIdAndWishlistId(long productId, long wishlistId);

    @Query("select new com.project.shoply.payload.response.WishlistItemResponse(" +
            "wi.product.id," +
            "wi.product.name," +
            "wi.product.price," +
            "wi.product.imageUrl) " +
            "FROM WishlistItem wi " +
            "WHERE wi.wishlist.id = :wishlistId AND wi.product.active = true")
    Set<WishlistItemResponse> findItemByWishlistId(long wishlistId);

    void deleteAllByWishlistId(long wishlistId);
}
