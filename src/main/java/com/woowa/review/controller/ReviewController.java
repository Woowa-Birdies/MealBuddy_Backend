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
import java.util.Map;

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
        // postId로 roomInfo(방번호,모집글 생성자ID)가져오기
        Map<String,Long> roomInfo = reviewService.getRoomInfo(postId);
        Long roomId = roomInfo.get("roomId");
        Long creatorId = roomInfo.get("userNo");
        log.info("roomId = {}",roomId);
        log.info("creatorId = {}",creatorId);

        // roomId(방번호)로 참여자ID 가져오기
        List<Long> userIdList = reviewService.getUserIdList(roomId);
        log.info("userIdList size = {}",userIdList.size());

        // 참여자ID로 참여자 정보 가져오기
        List<UserInfoDto> userInfoList = reviewService.getUserInfoByUserId(userIdList,creatorId);
        log.info("userInfoList size = {}",userInfoList.size());
        return ResponseEntity.ok(userInfoList);
    }
}
