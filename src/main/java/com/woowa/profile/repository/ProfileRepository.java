package com.woowa.profile.repository;

import com.woowa.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Review,Long> {
        @Query("SELECT review.reviewId, COUNT(review.reviewId) " +
            "FROM Review review " +
            "WHERE review.userId = :userId " +
            "GROUP BY review.reviewId")
        List<Object[]> findProfileByUserId(@Param("userId") Long userId);
}
