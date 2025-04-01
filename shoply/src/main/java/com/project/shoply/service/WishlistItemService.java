package com.project.shoply.service;

import com.project.shoply.entity.Product;
import com.project.shoply.entity.User;
import com.project.shoply.entity.Wishlist;
import com.project.shoply.entity.WishlistItem;
import com.project.shoply.exception.GenericException;
import com.project.shoply.exception.ResourceNotFoundException;
import com.project.shoply.repository.WishlistItemRepository;
import com.project.shoply.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishlistItemService {

    private final WishlistItemRepository wishlistItemRepository;
    private final WishlistRepository wishlistRepository;
    private final ProductService productService;

    // aggiunta di un elemento nella wishlist
    public String addWishlistItem(long wishlistId, long productId, UserDetails userDetails) {
        User user = (User) userDetails;
        long userId = user.getId();

        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist", "id", wishlistId));
        if (wishlist.getUser().getId() != userId)
            throw new GenericException(
                    "Wishlist ownership conflict: the wishlist does not belong to the specified user.",
                    HttpStatus.FORBIDDEN);
        if (wishlistItemRepository.existsByProductIdAndWishlistId(wishlistId, productId))
            throw new GenericException("This product is already in the wishlist", HttpStatus.CONFLICT);

        Product product = productService.findProductById(productId);
        if (!product.isActive())
            throw new DisabledException("Product is not active");

        WishlistItem wishlistItem = WishlistItem.builder()
                .wishlist(wishlist)
                .product(product)
                .build();
        wishlistItemRepository.save(wishlistItem);
        return "Wishlist item added";
    }

    // rimozione di un elemento dalla wishlist
    public String removeWishlistItem(long wishlistItemId, UserDetails userDetails) {
        User user = (User) userDetails;
        long userId = user.getId();

        WishlistItem wishlistItem = findWishlistItemById(wishlistItemId);
        if (wishlistItem.getWishlist().getUser().getId() != userId) {
            throw new GenericException(
                    "Conflict: You are not the owner of this wishlist item.",
                    HttpStatus.FORBIDDEN);
        }
        wishlistItemRepository.delete(wishlistItem);
        return "Wishlist item removed";
    }

    protected WishlistItem findWishlistItemById(long id) {
        return wishlistItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WishlistItem", "id", id));
    }
}
