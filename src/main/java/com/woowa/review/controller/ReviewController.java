package com.woowa.review.controller;

import com.woowa.review.domain.Review;
import com.woowa.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/review/save")
    public ResponseEntity<Void> saveReview(@RequestBody Review review) {
        try {
            reviewService.saveReview(review);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/review/userInfo/{postId}")
    public List<Long> getUserInfosByPostId(@PathVariable Long postId) {
        return reviewService.findUserIdsByPostId(postId);
    }
}
