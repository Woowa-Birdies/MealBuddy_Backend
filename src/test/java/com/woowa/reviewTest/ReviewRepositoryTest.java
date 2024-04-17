package com.woowa.reviewTest;

import com.woowa.review.domain.Review;
import com.woowa.review.repository.ReviewRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    @Transactional
    public void Save() {
        Review review = new Review();
        review.setManner(false);
        Review saveReview = reviewRepository.save(review);
        Assertions.assertThat(saveReview.getManner()).isEqualTo(false);
    }
}
