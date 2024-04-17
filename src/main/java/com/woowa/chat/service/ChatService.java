package com.woowa.chat.service;

import com.woowa.chat.domain.Chat;
import com.woowa.chat.domain.UserStatus;
import com.woowa.chat.domain.dto.ChatResponseDto;
import com.woowa.chat.domain.dto.MessageResponseDto;
import com.woowa.chat.domain.dto.StatusMessageDto;
import com.woowa.chat.domain.dto.StatusResponseDto;
import com.woowa.chat.repository.ChatRepository;
import com.woowa.chat.repository.UserStatusRepository;
import com.woowa.room.event.RoomJoinEvent;
import com.woowa.room.event.RoomLeaveEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserStatusRepository userStatusRepository;
    private final SimpMessagingTemplate brokerMessagingTemplate;

    public Slice<ChatResponseDto> getChats(final long roomId, final Pageable page, LocalDateTime dateTime){
        log.info("getChats() roomId: {}", roomId);
        return chatRepository.findChatsByRoomIdBefore(roomId, dateTime, page)
                .map(chat -> ChatResponseDto.builder()
                        .messageId(chat.getMessageId())
                        .message(chat.getMessage())
                        .sender(chat.getSender())
                        .roomId(chat.getRoomId())
                        .createAt(chat.getCreateAt())
                        .build());
    }

    public MessageResponseDto saveChat(final Long userId, final Long roomId, final String message){
        log.info("saveChat() userId: {}, roomId: {}", userId, roomId);

        return new MessageResponseDto().from(chatRepository.save(Chat.builder()
                .sender(userId)
                .roomId(roomId)
                .message(message)
                .build()));
    }

    public StatusResponseDto getUserStatus(final Long userId, final Long roomId){
        log.info("getUserStatus() userId: {}, roomId: {}", userId, roomId);
        List<UserStatus> userStatusList = userStatusRepository.findByRoomId(roomId);

        return StatusResponseDto.builder()
                .roomId(roomId)
                .joinUsers(userStatusList.stream()
                        .map(userStatus -> StatusMessageDto.builder()
                                .roomId(roomId)
                                .lastReadAt(userStatus.getLastReadAt())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public void setUserStatus(final Long userId, final Long roomId, final LocalDateTime dateTime){
        log.info("setUserStatus() userId: {}, roomId: {}", userId, roomId);

        userStatusRepository.save(createUserStatus(userId, roomId, dateTime));
    }



    @EventListener
    public void onUserJoin(final RoomJoinEvent roomJoinEvent){
        log.info("onUserJoin() roomJoinEvent: {}", roomJoinEvent);
        UserStatus userStatus = createUserStatus(roomJoinEvent.getUserId(), roomJoinEvent.getRoomId(), LocalDateTime.now());

        userStatusRepository.save(userStatus);

        Chat savedChat = chatRepository.save(Chat.builder()
                .sender(-1L)
                .roomId(roomJoinEvent.getRoomId())
                .message(roomJoinEvent.getUserName()+"님이 입장하셨습니다.")
                .build());

        brokerMessagingTemplate.convertAndSend("/sub/chat/room/" + roomJoinEvent.getRoomId(),
                savedChat);
    }

    @EventListener
    public void onUserLeave(final RoomLeaveEvent roomLeaveEvent){
        log.info("onUserLeave() roomLeaveEvent: {}", roomLeaveEvent);
        userStatusRepository.deleteByUserIdAndRoomId(roomLeaveEvent.getUserId(), roomLeaveEvent.getRoomId());

        Chat savedChat = chatRepository.save(Chat.builder()
                .sender(-1L)
                .roomId(roomLeaveEvent.getRoomId())
                .message(roomLeaveEvent.getUserName()+"님이 퇴장하셨습니다.")
                .build());

        brokerMessagingTemplate.convertAndSend("/sub/chat/room/" + roomLeaveEvent.getRoomId(),
                savedChat);
    }

    private static UserStatus createUserStatus(Long userId, Long roomId, LocalDateTime dateTime) {
        return UserStatus.builder()
                .userId(userId)
                .roomId(roomId)
                .lastReadAt(dateTime)
                .build();
    }
}
