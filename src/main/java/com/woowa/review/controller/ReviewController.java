package com.woowa.review.controller;

import com.woowa.review.domain.Review;
import com.woowa.review.domain.dto.UserInfoDto;
import com.woowa.review.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
        유저 후기 저장
     */
    @PostMapping("/api/review/save")
    public ResponseEntity<Void> saveReview(@RequestBody Review review) {
        try {
            reviewService.saveReview(review);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
        postId로 roomId 찾고 찾은 roomId로 userId 찾아서 리턴
     */
    @GetMapping("/api/review/userInfo/{postId}")
    public ResponseEntity<List<UserInfoDto>> getUserListByPostId(@PathVariable Long postId) {
        Long roomId = reviewService.getRoomId(postId);
        log.info("roomId : {}",roomId);

        List<Long> userIdList = reviewService.getUserIdList(roomId);
        log.info("userIdList size = {}",userIdList.size());

        List<UserInfoDto> userInfoList = reviewService.getUserInfoByUserId(userIdList);
        log.info("userInfoList size = {}",userInfoList.size());
        return ResponseEntity.ok(userInfoList);
    }

    @GetMapping("/test/{roomId}")
    public ResponseEntity<List<UserInfoDto>> myTest(@PathVariable Long roomId) {
        log.info("roomId : {}",roomId);
        List<Long> userIdList = reviewService.getUserIdList(roomId);
        log.info("userIdList size = {}",userIdList.size());

        List<UserInfoDto> userInfoList = reviewService.getUserInfoByUserId(userIdList);
        log.info("userInfoList size = {}",userInfoList.size());
        return ResponseEntity.ok(userInfoList);
    }
}
