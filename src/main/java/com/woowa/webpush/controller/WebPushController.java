package com.woowa.webpush.controller;

import com.woowa.webpush.domain.ApplyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@Slf4j
public class WebPushController {
    // TODO: repository 구현 후 저장

    @MessageMapping("/message") // 테스트용 이후에 변경
    @SendTo("/topic/messages")
    public ApplyDTO ApplyRequest(@RequestBody ApplyDTO request) {
        log.info("ApplyRequest = {} Path = {} ",request.getMessage(),WebPushController.class);

        // TODO: 들어온 request ApplyDTO 처리
        int postId = request.getPostId();

        return request;
    }
}
