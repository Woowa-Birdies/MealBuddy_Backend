package com.woowa.gather.controller;

import com.woowa.gather.domain.dto.AskListResponse;
import com.woowa.gather.domain.dto.AskRequest;
import com.woowa.gather.domain.dto.ListApiResponse;
import com.woowa.gather.domain.dto.UserPostListResponse;
import com.woowa.gather.exception.NonExistTypeException;
import com.woowa.gather.service.AskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AskController extends BaseAskController {
    private final AskService askService;

    @PostMapping("/ask")
    public ResponseEntity<Long> saveAsk(@RequestBody @Valid AskRequest askRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(askService.saveAsk(askRequest));
    }

    @DeleteMapping("/ask/{askId}")
    public ResponseEntity<Long> deleteAsk(@PathVariable Long askId) {
        return ResponseEntity.status(HttpStatus.OK).body(askService.deleteAsk(askId));
    }

    @GetMapping("/gather/ask/list/{postId}")
    public ListApiResponse<?> getApplicantList(@PathVariable Long postId) {
        return makeResponse(askService.getPostAskList(postId));
    }

    @GetMapping("/gather/list/{userId}")
    public ListApiResponse<UserPostListResponse> getUserPostList(@RequestParam int type, @PathVariable Long userId) {
        if (type > 2 || type < 0) {
            throw new NonExistTypeException("타입 범위는 [0,1,2]입니다");
        }

        return makeResponse(type, askService.getUserPostList(userId, type));
    }

    @GetMapping("/aks/list/{userId}")
    public ListApiResponse<AskListResponse> getAskList(@RequestParam int type, @PathVariable Long userId) {
        if (type > 2 || type < 0) {
            throw new NonExistTypeException("타입 범위는 [0,1,2]입니다");
        }

        return makeResponse(type, askService.getAskList(userId, type));
    }

}
