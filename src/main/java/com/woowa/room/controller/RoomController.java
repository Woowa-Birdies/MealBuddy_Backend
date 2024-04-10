package com.woowa.room.controller;

import com.woowa.room.domain.dto.RoomResponseDto;
import com.woowa.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
public class RoomController {

    private final RoomService roomService;
    /*
    // 채팅방 불러오기
    @GetMapping
    public List<RoomResponseDto> getRoomInfo(final @AuthenticationPrincipal UserDetails userDetails) {
        log.info("getRoomInfo() userId: {}", userId);

        return roomService.getRoomInfo(userId);
    }*/
}
