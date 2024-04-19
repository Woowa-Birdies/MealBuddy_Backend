package com.woowa.room.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class RoomLeaveEvent extends RoomNoticeEvent {
    public RoomLeaveEvent(Long roomId, Long userId, Long postId, String userName) {
        super(roomId, userId, postId, userName);
    }
}
