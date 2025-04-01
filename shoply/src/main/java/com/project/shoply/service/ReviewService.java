package com.project.shoply.service;

import com.project.shoply.entity.Product;
import com.project.shoply.entity.Review;
import com.project.shoply.entity.User;
import com.project.shoply.entity.enumerated.ReviewCondition;
import com.project.shoply.exception.GenericException;
import com.project.shoply.exception.ResourceNotFoundException;
import com.project.shoply.payload.request.ReviewRequest;
import com.project.shoply.payload.response.ReviewReportingResponse;
import com.project.shoply.payload.response.ReviewResponse;
import com.project.shoply.repository.ReviewRepository;
import com.project.shoply.repository.ReviewStatusRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductService productService;
    private final ReviewStatusRepository statusRepository;

    public String createReview(UserDetails userDetails, ReviewRequest request) {
        User user = (User) userDetails;
        Product product = productService.findProductById(request.getProductId());

        Review review = Review.builder()
                .comment(request.getComment())
                .rating(request.getRating())
                .user(user)
                .product(product)
                .enable(true)
                .report(false)
                .status(statusRepository.findByReviewStatusDefaultTrue())
                .build();

        reviewRepository.save(review);
        return "Review created";
    }

    public List<ReviewResponse> getReviewByProductId(Long productId) {
        if (!productService.existsProductById(productId))
            throw new ResourceNotFoundException("Product", "id", productId);
        return reviewRepository.getReviewByProductId(productId);
    }

    @Transactional
    public String reportingReview(long reviewId) {
        Review review = findReviewById(reviewId);

        if (review.getStatus() == null) {
            throw new GenericException(
                    "The value cannot be null.",
                    HttpStatus.FORBIDDEN);
        }

        if (review.getStatus().getReviewCondition().equals(ReviewCondition.REHABILITATED)) {
            throw new GenericException(
                    "The review has been rehabilitated and cannot be reported again.",
                    HttpStatus.CONFLICT);
        }

        if (review.isReport()) {
            throw new GenericException(
                    "The review has already been reported.",
                    HttpStatus.CONFLICT);
        }
        review.setReport(true);
        review.setStatus(statusRepository.findByReviewCondition(ReviewCondition.PENDING));
        return "Review reporting";
    }

    public List<ReviewReportingResponse> getPendingReview() {
        return reviewRepository.getPendingReview();
    }

    @Transactional
    public String rehabilitatedOrCensuredReview(ReviewCondition reviewCondition, long reviewId) {
        Review review = findReviewById(reviewId);

        if (!reviewCondition.equals(ReviewCondition.CENSURED) && !reviewCondition.equals(ReviewCondition.REHABILITATED)) {
            throw new GenericException("Review condition value is not valid for this function", HttpStatus.BAD_REQUEST);
        }

        if (reviewCondition.equals(ReviewCondition.CENSURED)) {
            review.setStatus(statusRepository.findByReviewCondition(ReviewCondition.CENSURED));
            review.setEnable(false);
            return "Review censured";
        }

        review.setStatus(statusRepository.findByReviewCondition(ReviewCondition.REHABILITATED));
        return "Review rehabilitated";
    }

    protected Review findReviewById(long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", id));
    }

}
