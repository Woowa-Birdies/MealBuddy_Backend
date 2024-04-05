package com.woowa.room.service;

import com.woowa.room.domain.Room;
import com.woowa.room.domain.dto.RoomResponseDto;
import com.woowa.room.repository.RoomRepository;
import com.woowa.room.repository.RoomUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomUserRepository roomUserRepository;

    public List<RoomResponseDto> getRoomInfo(final int userId) {
        return null;
    }

}
