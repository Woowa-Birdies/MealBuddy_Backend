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
                .select(Projections.constructor(RoomResponseDto.class))
                .from(roomUser)
                .where(roomUser.user.id.eq(userId))
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


}
