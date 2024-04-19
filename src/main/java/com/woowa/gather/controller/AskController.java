package com.woowa.gather.controller;

import com.woowa.gather.domain.dto.*;
import com.woowa.gather.domain.enums.AskStatus;
import com.woowa.gather.exception.AskErrorCode;
import com.woowa.gather.exception.AskException;
import com.woowa.gather.service.AskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class AskController extends BaseAskController {
    private final AskService askService;

    @PostMapping("/ask")
    public ResponseEntity<AskResponse> saveAsk(@RequestBody @Valid AskRequest askRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(askService.saveAsk(askRequest));
    }

    @DeleteMapping("/ask/{askId}")
    public ResponseEntity<Long> deleteAsk(@PathVariable Long askId) {
        return ResponseEntity.status(HttpStatus.OK).body(askService.deleteAsk(askId));
    }

    @PatchMapping("/ask")
    public ResponseEntity<AskResponse> acceptAsk(@RequestBody @Valid AskUpdate askUpdate) {
        if (askUpdate.getAskStatus() == AskStatus.PARTICIPATION) {
            throw new AskException(AskErrorCode.INVALID_STATUS_VALUE);
        }
        return ResponseEntity.ok().body(askService.changeAskStatus(askUpdate));
    }

    @GetMapping("/gather/ask/list/{postId}")
    public ListApiResponse<PostAskListResponse> getAskList(@PathVariable Long postId, @RequestParam int type) {
        checkType(type);

        return makeResponse(askService.getPostAskList(postId, type));
    }

    @GetMapping("/gather/list/{userId}")
    public ListApiResponse<PostListResponse> getUserPostList(@RequestParam int type, @PathVariable Long userId) {
        checkType(type);

        return makeUserPostResponse(type, askService.getUserPostList(userId, type));
    }

    @GetMapping("/ask/list/{userId}")
    public ListApiResponse<AskListResponse> getUserAskList(@RequestParam int type, @PathVariable Long userId) {
        checkType(type);
        return makeUserAskResponse(type, askService.getAskList(userId, type));
    }

    private static void checkType(int type) {
        if (type > 2 || type < 0) {
            throw new AskException(AskErrorCode.INVALID_PARAMETER_TYPE);
        }
    }

}
