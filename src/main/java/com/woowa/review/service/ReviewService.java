package com.woowa.review.service;

import com.woowa.review.domain.Review;
import com.woowa.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

    public List<Long> findUserIdsByPostId(Long postId) {
        List<Review> users = reviewRepository.findByPostId(postId);
        return users.stream()
                .map(Review::getUserId)
                .collect(Collectors.toList());
    }
}
