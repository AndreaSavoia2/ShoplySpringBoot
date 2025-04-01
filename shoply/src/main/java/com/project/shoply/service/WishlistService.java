package com.project.shoply.service;

import com.project.shoply.entity.User;
import com.project.shoply.entity.Wishlist;
import com.project.shoply.exception.GenericException;
import com.project.shoply.exception.ResourceNotFoundException;
import com.project.shoply.payload.response.WishlistItemResponse;
import com.project.shoply.payload.response.WishlistResponse;
import com.project.shoply.repository.WishlistItemRepository;
import com.project.shoply.repository.WishlistRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private static final Logger log = LoggerFactory.getLogger(WishlistService.class);
    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;

    public String CreateWishlist(String title, UserDetails userDetails) {
        User user = (User) userDetails;
        Wishlist wishlist = Wishlist.builder()
                .title(title.trim())
                .user(user)
                .build();
        wishlistRepository.save(wishlist);
        return "wishlist created";
    }

    public List<Wishlist> findAllByUserId(UserDetails userDetails) {
        User user = (User) userDetails;
        return wishlistRepository.findAllByUserId(user.getId());
    }

    // ricerca della wishlist singola con tutti gli item al suo interno
    public WishlistResponse getWishlistDetailsItems(UserDetails userDetails, long wishlistId) {
        User user = (User) userDetails;
        long userId = user.getId();

        Wishlist wishlist = findWishlistById(wishlistId);
        if (wishlist.getUser().getId() != userId)
            throw new GenericException(
                    "Wishlist ownership conflict: the wishlist does not belong to the specified user.",
                    HttpStatus.FORBIDDEN);

        Set<WishlistItemResponse> wishlistItems = wishlistItemRepository.findItemByWishlistId(wishlist.getId());

        return WishlistResponse.builder()
                .id(wishlist.getId())
                .title(wishlist.getTitle())
                .wishlistItems(wishlistItems)
                .build();
    }

    //cancellazione della wishlist
    @Transactional
    public String deleteWishList(UserDetails userDetails, long wishlistId) {
        User user = (User) userDetails;
        long userId = user.getId();

        // cerco la wishlist controllo che la wish list appartenga all'utente
        Wishlist wishlist = findWishlistById(wishlistId);
        if (wishlist.getUser().getId() != userId)
            throw new GenericException(
                    "Wishlist ownership conflict: the wishlist does not belong to the specified user.",
                    HttpStatus.FORBIDDEN);

        // Prima cancello tutte le wishlistItem legata alla wishlist
        wishlistItemRepository.deleteAllByWishlistId(wishlist.getId());
        // poi cancello la wishlist
        wishlistRepository.delete(wishlist);
        return "wishlist deleted";
    }

    protected Wishlist findWishlistById(long wishlistId) {
        return wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist", "id", wishlistId));
    }
}
