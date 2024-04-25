package com.woowa.room.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.woowa.room.domain.dto.JoinRequestDto;
import com.woowa.room.domain.dto.RoomResponseDto;
import com.woowa.room.service.RoomService;
import com.woowa.user.domain.dto.CustomOAuth2User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
public class RoomController {

	private final RoomService roomService;

    // 채팅방 불러오기
    @GetMapping
    public ResponseEntity<List<RoomResponseDto>> getRoomInfo(final @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        log.info("getRoomInfo() userId: {}", customOAuth2User.getUserId());

        return ResponseEntity.ok(roomService.getRoomInfo(customOAuth2User.getUserId()));
    }

    @PostMapping("/join")
    public ResponseEntity<RoomResponseDto> joinRoom(final @AuthenticationPrincipal CustomOAuth2User customOAuth2User, @RequestBody JoinRequestDto joinRequestDto) {
        log.info("joinRoom() userId: {}, postId: {}", customOAuth2User.getUserId(), joinRequestDto.postId());

        //return roomService.joinRoom(customOAuth2User.getUserId(), joinRequestDto.postId());
        return ResponseEntity.ok(roomService.joinRoom(customOAuth2User.getUserId(), joinRequestDto.postId()));
    }

    @DeleteMapping("/kick/{roomId}/{userId}")
    public ResponseEntity<Void> kickUser(final @AuthenticationPrincipal CustomOAuth2User customOAuth2User, final @PathVariable long roomId, final @PathVariable long userId) {
        log.info("kickUser() userId: {}, roomId: {}, kickUserId: {}", customOAuth2User.getUserId(), roomId, userId);

        roomService.kickUser(customOAuth2User.getUserId(), roomId, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/leave/{roomId}")
    public ResponseEntity<Void> leaveRoom(final @AuthenticationPrincipal CustomOAuth2User customOAuth2User, final @PathVariable long roomId) {
        log.info("leaveRoom() userId: {}, roomId: {}", customOAuth2User.getUserId(), roomId);

        roomService.leaveRoom(customOAuth2User.getUserId(), roomId);

        return ResponseEntity.noContent().build();
    }

}
