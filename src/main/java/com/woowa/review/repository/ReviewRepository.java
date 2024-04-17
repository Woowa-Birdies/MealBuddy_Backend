package com.woowa.review.repository;

import com.woowa.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findByPostId(Long postId);
}


