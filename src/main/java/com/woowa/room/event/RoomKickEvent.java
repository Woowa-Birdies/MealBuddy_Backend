package com.woowa.room.event;

public class RoomKickEvent extends RoomNoticeEvent{
    public RoomKickEvent(Long roomId, Long userId, String userName) {
        super(roomId, userId, userName);
    }
}
