package com.woowa.room.repository.support;

import com.woowa.room.domain.dto.RoomResponseDto;

import java.util.List;

public interface RoomUserRepositoryCustom {

    //채팅방 목록 조회
    List<RoomResponseDto> getRoomInfo(long userId);
    //채팅방 나가기 (방장이 나가도 방 유지)
    boolean leaveRoom(long userId, long roomId);
    //채팅방 삭제
    void deleteRoom(long roomId);


}
