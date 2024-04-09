package com.woowa.room.repository.support;

import com.woowa.room.domain.dto.RoomResponseDto;

import java.util.List;

public interface RoomRepositoryCustom {
    List<RoomResponseDto> findRoomResponseDtosByUserId(long userId);
    void deleteRoomUserByUserId(long userId, long roomId);
    //room 참가 가능 여부
    boolean isJoinable(long userId, long postId);

}
