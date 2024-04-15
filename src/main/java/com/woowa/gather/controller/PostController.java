package com.woowa.gather.controller;

import com.woowa.gather.domain.Post;
import com.woowa.gather.domain.dto.*;
import com.woowa.gather.domain.enums.Age;
import com.woowa.gather.domain.enums.FoodType;
import com.woowa.gather.domain.enums.Gender;
import com.woowa.gather.domain.enums.PostStatus;
import com.woowa.gather.service.PostQueryService;
import com.woowa.gather.service.PostReadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    private final PostQueryService postQueryService;
    private final PostReadService postReadService;

    @PostMapping("/post")
    public ResponseEntity<Long> create(@RequestBody @Valid PostCreateDto postCreateDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postQueryService.create(postCreateDto));
    }

    @PatchMapping("/post")
    public ResponseEntity<Long> update(@RequestBody @Valid PostUpdateDto postUpdateDto) {
        return ResponseEntity.ok(postQueryService.update(postUpdateDto));
    }

    @PatchMapping("/post/completion/{postId}")
    public ResponseEntity<Long> updatePostStatusToCompletion(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(postQueryService.updatePostStatus(postId, PostStatus.COMPLETION));
    }

    @PatchMapping("/post/ongoing/{postId}")
    public ResponseEntity<Long> updatePostStatusToOngoing(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(postQueryService.updatePostStatus(postId, PostStatus.ONGOING));
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<Long> delete(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(postQueryService.delete(postId));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<PostDetailsResponseDto> findPostDetails(@PathVariable("postId") Long postId, @RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(postReadService.findPostDetailsByPostIdAndUserId(postId, userId));
    }

    @GetMapping("/post/over/{withinDate}")
    public ResponseEntity<ListApiResponse> findDuePosts(@PathVariable("withinDate") int withinDate) {
        List<PostListResponse> duePostListResponseList = postReadService.findDuePosts(withinDate);
        ListApiResponse duePostListApiResponse = ListApiResponse.<PostListResponse>builder()
                .resultCount(duePostListResponseList.size())
                .ongoing(duePostListResponseList)
                .build();
        return ResponseEntity.ok(duePostListApiResponse);
    }

    @GetMapping("/post/filter")
    public ResponseEntity<ListApiResponse> findFilteringPosts(@RequestParam(required = false) List<Integer> dateTypes,
                                                              @RequestParam(required = false) List<FoodType> foodTypes,
                                                              @RequestParam(required = false) List<Age> ages,
                                                              @RequestParam(required = false) List<Gender> genders) {

        // foodTypes, ages, genders 쿼리 파라미터 null 체크 및 처리
        if (foodTypes == null) foodTypes = new ArrayList<>();
        if (ages == null) ages = new ArrayList<>();
        if (genders == null) genders = new ArrayList<>();

//        // foodTypes 상태 logging
//        if (foodTypes != null) {
//            logger.info("Received foodTypes: {}", foodTypes);
//        } else {
//            logger.info("Received foodTypes is null or empty");
//        }

        List<Post> filteringPostList = postReadService.filterPosts(dateTypes, foodTypes, ages, genders);

        // Post 엔티티 리스트를 PostListResponse DTO 리스트로 변환
        List<PostListResponse> filteringPostListResponseList = filteringPostList.stream()
                .map(post -> PostListResponse.builder()
                        .postId(post.getId())
                        .userId(post.getUser().getId())
                        .foodTypeTag(post.getFoodTypeTag())
                        .genderTag(post.getGenderTag())
                        .ageTag(post.getAgeTag())
                        .address(post.getLocation().getAddress())
                        .place(post.getLocation().getPlace())
                        .participantTotal(post.getParticipantTotal())
                        .participantCount(post.getParticipantCount())
                        .postStatus(post.getPostStatus())
                        .meetAt(post.getMeetAt())
                        .closeAt(post.getCloseAt())
                        .createdAt(post.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        ListApiResponse filteringPostListApiResponse = ListApiResponse.<PostListResponse>builder()
                .resultCount(filteringPostListResponseList.size())
                .ongoing(filteringPostListResponseList)
                .build();
        return ResponseEntity.ok(filteringPostListApiResponse);
    }

}