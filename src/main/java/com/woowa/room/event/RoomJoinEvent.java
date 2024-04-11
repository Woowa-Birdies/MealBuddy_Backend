package com.woowa.room.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class RoomJoinEvent extends RoomNoticeEvent{
    public RoomJoinEvent(Long roomId, Long userId, String userName) {
        super(roomId, userId, userName);
    }
}
