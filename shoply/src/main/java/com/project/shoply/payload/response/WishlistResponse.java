package com.project.shoply.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class WishlistResponse {

    private long id;
    private String title;
    private Set<WishlistItemResponse> wishlistItems;

}
