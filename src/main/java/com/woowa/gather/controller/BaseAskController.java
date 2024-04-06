package com.woowa.gather.controller;

import com.woowa.gather.domain.dto.ListApiResponse;

import java.util.List;

abstract class BaseAskController {

    public <T> ListApiResponse<T> makeResponse(int type, List<T> result) {
        return type == 0 ? makeOngoingListResponse(result)
                : type == 1 ? makeCompletedListResponse(result)
                : makeClosedListResponse(result);
    }

    public <T> ListApiResponse<T> makeOngoingListResponse(List<T> result) {
        return ListApiResponse.<T>builder()
                .resultCount(result.size())
                .ongoing(result)
                .build();
    }

    public <T> ListApiResponse<T> makeCompletedListResponse(List<T> result) {
        return ListApiResponse.<T>builder()
                .resultCount(result.size())
                .completion(result)
                .build();
    }

    public <T> ListApiResponse<T> makeClosedListResponse(List<T> result) {
        return ListApiResponse.<T>builder()
                .resultCount(result.size())
                .closed(result)
                .build();
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
