package com.woowa.room.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class RoomJoinEvent extends RoomNoticeEvent{
    public RoomJoinEvent(Long roomId, Long userId, Long postId, String userName) {
        super(roomId, userId, postId, userName);
    }
}
