package com.woowa.gather.controller;

import com.woowa.gather.domain.dto.ListApiResponse;

import java.util.List;

abstract class BaseAskController {

    // 유저의 신청 리스트
    public <T> ListApiResponse<T> makeListResponse(int listType, int type, List<T> result, int page, int size, long totalElements, int totalPages) {
        return new ListApiResponse<>(listType, type, result, page, size, totalElements, totalPages);
    }

//    public <T> ListResponse<T> makeOngoingAndCompletedListResponse(List<T> ongoing, List<T> completion) {
//        return ListResponse.<T>builder()
//                .ongoing(ongoing)
//                .completion(completion)
//                .build();
//    }
//
//    public <T> ListResponse<T> makeOngoingAndClosedListResponse(List<T> ongoing, List<T> closed) {
//        return ListResponse.<T>builder()
//                .ongoing(ongoing)
//                .closed(closed)
//                .build();
//    }
//
//    public <T> ListResponse<T> makeCompletedAndClosedListResponse(List<T> completion, List<T> closed) {
//        return ListResponse.<T>builder()
//                .completion(completion)
//                .closed(closed)
//                .build();
//    }
//
//    public <T> ListResponse<T> makeAllListResponse(List<T> ongoing, List<T> completion, List<T> closed) {
//        return ListResponse.<T>builder()
//                .ongoing(ongoing)
//                .completion(completion)
//                .closed(closed)
//                .build();
//    }
}
