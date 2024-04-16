package com.woowa.chat.controller;


import com.woowa.chat.domain.dto.*;
import com.woowa.chat.service.ChatService;
import com.woowa.user.domain.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/chat")
    public ResponseEntity<Slice<ChatResponseDto>> saveChat(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                           @RequestBody ChatRequestDto chatRequestDto){
        log.info("saveChat() roomId: {}", chatRequestDto.roomId());
        Pageable page = PageRequest.of(chatRequestDto.page(), chatRequestDto.offset());
        return ResponseEntity.ok(chatService.getChats(chatRequestDto.roomId(), page, chatRequestDto.entryAt()));
    }

    @MessageMapping("/chat/{roomId}")
    @SendTo("/sub/chat/{roomId}")
    public ResponseEntity<MessageResponseDto> chat(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                   @RequestBody MessageRequestDto messageRequestDto){
        log.info("chat() roomId: {}", messageRequestDto.roomId());
        return ResponseEntity.ok(
                chatService.saveChat(customOAuth2User.getUserId(), messageRequestDto.roomId(), messageRequestDto.message()));
    }

    @MessageMapping("/chat/status/{roomId}")
    @SendTo("/sub/chat/status/{roomId}")
    public ResponseEntity<StatusResponseDto> userStatus(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                        @RequestBody StatusMessageDto statusMessageDto){
        log.info("userStatus() roomId: {}", statusMessageDto.getRoomId());
        return ResponseEntity.ok(chatService.getUserStatus(customOAuth2User.getUserId(), statusMessageDto.getRoomId()));
    }

}
