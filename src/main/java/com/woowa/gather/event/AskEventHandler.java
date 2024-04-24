package com.woowa.gather.event;

import com.woowa.gather.service.AskService;
import com.woowa.room.event.RoomJoinEvent;
import com.woowa.room.event.RoomLeaveEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AskEventHandler {
    private final AskService askService;

    @EventListener
    public void participate(RoomJoinEvent roomJoinEvent) {
        if (askService.checkIfPostNotExistsByUser(roomJoinEvent.getPostId(), roomJoinEvent.getUserId())) {
            askService.participate(roomJoinEvent.getPostId(), roomJoinEvent.getUserId());
        }
    }

    @EventListener
    public void deleteAsk(RoomLeaveEvent roomLeaveEvent) {
        askService.deleteAsk(roomLeaveEvent.getPostId(), roomLeaveEvent.getUserId());
    }

    @EventListener
    public void deleteKickedUserAsk(RoomLeaveEvent roomLeaveEvent) {
        askService.deleteAsk(roomLeaveEvent.getPostId(), roomLeaveEvent.getUserId());
    }
}
