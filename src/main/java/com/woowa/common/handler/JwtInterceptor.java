package com.woowa.common.handler;

import com.woowa.chat.exception.ChatErrorCode;
import com.woowa.chat.exception.CustomChatException;
import com.woowa.user.domain.dto.CustomOAuth2User;
import com.woowa.user.domain.dto.UserDTO;
import com.woowa.user.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtInterceptor implements ChannelInterceptor {
    private final JWTUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
            validateToken(authorizationHeader);
            SecurityContextHolder.getContext().setAuthentication(getAuthentication(authorizationHeader));
        }
        return message;
    }

    private void validateToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.error("No JWT token found in request headers");
            //todo: error handling
            throw new CustomChatException(ChatErrorCode.UNVALID_HEADER);
        }
        String token = authorizationHeader.substring("Bearer ".length());
        try {
            jwtUtil.isExpired(token);
        } catch (Exception e) {
            log.error("Error validating JWT token: {}", e.getMessage());
            //todo : error handling
            throw new CustomChatException(ChatErrorCode.UNVALID_USER);
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String accessToken) {
        Long userId = jwtUtil.getUserId(accessToken);
        String role = jwtUtil.getRole(accessToken);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(new UserDTO(userId, role));

        return new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
    }
}
