package com.woowa.gather.controller;

import com.woowa.gather.domain.dto.*;
import com.woowa.gather.domain.enums.AskStatus;
import com.woowa.gather.exception.AskErrorCode;
import com.woowa.gather.exception.AskException;
import com.woowa.gather.service.AskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
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
    public ResponseEntity<ListResponse> getAskList(
            @PathVariable Long postId,
            @RequestParam int type,
            @RequestParam int page,
            @RequestParam(required = false, defaultValue = "3") int pageSize,
            @RequestParam(required = false, defaultValue = "0") int totalPages) {
        checkType(type);

        return ResponseEntity.ok()
                .body(askService.getPostAskList(postId, type, page, pageSize, totalPages));
    }

    @GetMapping("/gather/list/{userId}")
    public ListApiResponse<PostListResponse> getUserPostList(
            @RequestParam int type,
            @RequestParam int page,
            @RequestParam(required = false, defaultValue = "3") int pageSize,
            @RequestParam(required = false, defaultValue = "0") int totalPages,
            @PathVariable Long userId) {
        checkType(type);

        Page<PostListResponse> list = askService.getUserPostList(userId, type, page, pageSize, totalPages);

        return makeListResponse(0, type, list.toList(), list.getPageable().getPageNumber(), pageSize, list.getTotalElements(), list.getTotalPages());
    }

    @GetMapping("/ask/list/{userId}")
    public ListApiResponse<AskListResponse> getUserAskList(
            @RequestParam int type,
            @RequestParam int page,
            @RequestParam(required = false, defaultValue = "3") int pageSize,
            @RequestParam(required = false, defaultValue = "0") int totalPages,
            @PathVariable Long userId) {
        checkType(type);

        Page<AskListResponse> list = askService.getAskList(userId, type, page, pageSize, totalPages);

        return makeListResponse(1, type, list.toList(), list.getPageable().getPageNumber(), pageSize, list.getTotalElements(), list.getTotalPages());
    }

    private static void checkType(int type) {
        if (type > 2 || type < 0) {
            throw new AskException(AskErrorCode.INVALID_PARAMETER_TYPE);
        }
    }

}
