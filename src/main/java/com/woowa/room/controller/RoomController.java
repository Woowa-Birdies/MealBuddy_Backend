package com.woowa.room.controller;

import com.woowa.room.domain.dto.JoinRequestDto;
import com.woowa.room.domain.dto.RoomResponseDto;
import com.woowa.room.service.RoomService;
import com.woowa.user.domain.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
public class RoomController {

    private final RoomService roomService;

    // 채팅방 불러오기
    @GetMapping
    public List<RoomResponseDto> getRoomInfo(final @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        log.info("getRoomInfo() userId: {}", customOAuth2User.getUserId());

        return roomService.getRoomInfo(customOAuth2User.getUserId());
    }

    @PostMapping("/join")
    public RoomResponseDto joinRoom(final @AuthenticationPrincipal CustomOAuth2User customOAuth2User, @RequestBody JoinRequestDto joinRequestDto) {
        log.info("joinRoom() userId: {}, roomId: {}", customOAuth2User.getUserId(), joinRequestDto.postId());

        return roomService.joinRoom(customOAuth2User.getUserId(), joinRequestDto.postId());
    }

    @DeleteMapping("/kick/{roomId}/{userId}")
    public void kickUser(final @AuthenticationPrincipal CustomOAuth2User customOAuth2User, final @PathVariable long roomId, final @PathVariable long userId) {
        log.info("kickUser() userId: {}, roomId: {}, kickUserId: {}", customOAuth2User.getUserId(), roomId, userId);

        roomService.kickUser(customOAuth2User.getUserId(), roomId, userId);
    }

    @DeleteMapping("/leave/{roomId}")
    public void leaveRoom(final @AuthenticationPrincipal CustomOAuth2User customOAuth2User, final @PathVariable long roomId) {
        log.info("leaveRoom() userId: {}, roomId: {}", customOAuth2User.getUserId(), roomId);

        roomService.leaveRoom(customOAuth2User.getUserId(), roomId);
    }

}
