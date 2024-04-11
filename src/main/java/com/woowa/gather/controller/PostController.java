package com.woowa.gather.controller;

import com.woowa.gather.domain.dto.*;
import com.woowa.gather.domain.enums.PostStatus;
import com.woowa.gather.service.PostQueryService;
import com.woowa.gather.service.PostReadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
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
        List<PostListResponse> duePostList = postReadService.findDuePosts(withinDate);
        ListApiResponse duePostListApiResponse = ListApiResponse.<PostListResponse>builder()
                .resultCount(duePostList.size())
                .ongoing(duePostList)
                .build();
        return ResponseEntity.ok(duePostListApiResponse);
    }

}