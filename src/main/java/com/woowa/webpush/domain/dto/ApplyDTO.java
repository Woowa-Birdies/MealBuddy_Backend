package com.woowa.webpush.domain;

public class ApplyDTO {
    // TODO: 클라이언트에서 어떤 값을 보내는 지 승현님과 소통 후 구성
    private String message;
    private int postId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
