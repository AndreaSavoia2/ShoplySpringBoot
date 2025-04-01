package com.project.shoply.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponse {

    private long id;
    private Integer rating;
    private String comment;
    private String username;
    private boolean reported;

}
