package com.project.shoply.repository;

import com.project.shoply.entity.ReviewStatus;
import com.project.shoply.entity.enumerated.ReviewCondition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewStatusRepository extends JpaRepository<ReviewStatus, Long> {

    ReviewStatus findByReviewStatusDefaultTrue();

    ReviewStatus findByReviewCondition(ReviewCondition reviewCondition);
}
