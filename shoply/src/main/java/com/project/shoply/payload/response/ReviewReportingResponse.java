package com.project.shoply.payload.response;

import com.project.shoply.entity.enumerated.ReviewCondition;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewReportingResponse {

    private long reviewId;
    private String username;
    private String comment;
    private long productId;
    private String productName;
    private ReviewCondition status;

}
