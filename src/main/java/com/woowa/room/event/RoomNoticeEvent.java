package com.woowa.room.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class RoomNoticeEvent {
    protected Long roomId;
    protected Long userId;
    protected String userName;
}
