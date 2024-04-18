package com.woowa.room.repository.support;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woowa.room.domain.dto.RoomResponseDto;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.woowa.room.domain.QRoom.room;
import static com.woowa.room.domain.QRoomUser.roomUser;

@Slf4j
@Repository
public class RoomUserRepositoryImpl implements RoomUserRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public RoomUserRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<RoomResponseDto> getRoomInfo(long userId) {
        return queryFactory
                .select(Projections.constructor(RoomResponseDto.class, room.id, room.roomName, roomUser.id.count()))
                .from(roomUser)
                .join(roomUser.room, room)
                .where(roomUser.user.id.eq(userId))
                .groupBy(room.id)
                .fetch();
    }

    @Override
    public boolean leaveRoom(long userId, long roomId) {
        return queryFactory
                .delete(roomUser)
                .where(roomUser.user.id.eq(userId)
                        .and(roomUser.room.id.eq(roomId)))
                .execute() > 0;
    }
    @Override
    public boolean kickUser(long userId, long roomId, long targetUserId) {
        return queryFactory
                .delete(roomUser)
                .where(roomUser.user.id.eq(targetUserId)
                        .and(roomUser.room.id.eq(roomId))
                        .and(room.user.id.eq(userId)))
                .execute() > 0;
    }

    @Override
    public void deleteRoom(long roomId) {
        queryFactory
                .delete(roomUser)
                .where(roomUser.room.id.eq(roomId))
                .execute();
    }
}
