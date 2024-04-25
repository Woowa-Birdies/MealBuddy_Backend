package com.woowa.review.repository;

import com.woowa.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    @Query("SELECT r.userId, " +
            "SUM(CASE WHEN r.punctuality = TRUE THEN 1 ELSE 0 END) AS punctuality_true_count, " +
            "SUM(CASE WHEN r.punctuality = FALSE THEN 1 ELSE 0 END) AS punctuality_false_count, " +
            "SUM(CASE WHEN r.sociability = TRUE THEN 1 ELSE 0 END) AS sociability_true_count, " +
            "SUM(CASE WHEN r.sociability = FALSE THEN 1 ELSE 0 END) AS sociability_false_count, " +
            "SUM(CASE WHEN r.manner = TRUE THEN 1 ELSE 0 END) AS manner_true_count, " +
            "SUM(CASE WHEN r.manner = FALSE THEN 1 ELSE 0 END) AS manner_false_count, " +
            "SUM(CASE WHEN r.reply = TRUE THEN 1 ELSE 0 END) AS reply_true_count, " +
            "SUM(CASE WHEN r.reply = FALSE THEN 1 ELSE 0 END) AS reply_false_count " +
            "FROM Review r WHERE r.userId = :userId GROUP BY r.userId")
    List<Object[]> countReviewAttributesByUserId(@Param("userId") Long userId);
}


