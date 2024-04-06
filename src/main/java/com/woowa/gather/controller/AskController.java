package com.woowa.gather.controller;

import com.woowa.gather.domain.dto.AskListResponse;
import com.woowa.gather.domain.dto.AskRequest;
import com.woowa.gather.domain.dto.ListApiResponse;
import com.woowa.gather.domain.dto.UserPostListResponse;
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
    public ResponseEntity<Integer> ask(@RequestBody @Valid AskRequest askRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(askService.ask(askRequest));
    }

//    @GetMapping("/test")
//    public ListApiResponse<AskListResponse> test() {
//        return makeOngoingListResponse(askService.getAskList(1, 1));
//    }

    @GetMapping("/gather/list/{userId}")
    public ListApiResponse<UserPostListResponse> getUserPostList(@RequestParam int type, @PathVariable Long userId) {
        return makeResponse(type, askService.getUserPostList(userId, type));
    }

}
