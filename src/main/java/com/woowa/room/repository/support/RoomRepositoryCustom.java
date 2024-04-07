package com.woowa.room.repository.support;

import com.woowa.room.domain.dto.RoomResponseDto;

import java.util.List;

public interface RoomRepositoryCustom {
    List<RoomResponseDto> findRoomResponseDtosByUserId(int userId);
    void deleteRoomUserByUserId(int userId, long roomId);

}
