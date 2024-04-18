package com.woowa.room.event;

public class RoomKickEvent extends RoomNoticeEvent{
    public RoomKickEvent(Long roomId, Long userId, Long postId, String userName) {
        super(roomId, userId, postId, userName);
    }
}
