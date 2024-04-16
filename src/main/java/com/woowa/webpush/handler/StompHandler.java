package com.woowa.webpush;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

/**
 * 메세지 처리과정에서 필요한 부분이 있는지 승현님과 소통 후 삭제 or 수정
 */
@Component
public class StompHandler implements ChannelInterceptor {
    // TODO: jwt 토큰

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        return ChannelInterceptor.super.preSend(message, channel);
    }
}
