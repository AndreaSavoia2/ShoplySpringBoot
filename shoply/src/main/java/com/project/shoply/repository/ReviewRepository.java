package com.project.shoply.repository;

import com.project.shoply.entity.Review;
import com.project.shoply.payload.response.ReviewReportingResponse;
import com.project.shoply.payload.response.ReviewResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT new com.project.shoply.payload.response.ReviewResponse(" +
            "r.id," +
            "r.rating," +
            "r.comment, " +
            "r.user.username, " +
            "r.report) FROM Review r " +
            "WHERE r.product.id = :productId " +
            "AND r.enable = true")
    List<ReviewResponse> getReviewByProductId(Long productId);

    @Query("SELECT new com.project.shoply.payload.response.ReviewReportingResponse(" +
            "r.id," +
            "r.user.username," +
            "r.comment, " +
            "r.product.id, " +
            "r.product.name, " +
            "r.status.reviewCondition" +
            ") FROM Review r " +
            "WHERE r.report = true AND r.status.reviewCondition = 'PENDING'")
    List<ReviewReportingResponse> getPendingReview();
}
