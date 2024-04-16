package com.woowa.profile.repository;

import com.woowa.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProfileRepository extends JpaRepository<Review,Long> {
        @Query("SELECT review.review_Id, COUNT(review.review_Id) " +
            "FROM Review review " +
            "WHERE review.user_Id = :user_Id " +
            "GROUP BY review.review_Id")
        List<Object[]> findProfileByUserId(Long user_Id);
}
