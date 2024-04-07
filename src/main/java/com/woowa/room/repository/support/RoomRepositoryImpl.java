package com.woowa.room.repository.support;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woowa.room.domain.dto.RoomResponseDto;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.woowa.room.domain.QRoom.room;
import static com.woowa.room.domain.QRoomUser.roomUser;
import static com.woowa.user.domain.QUser.user;

public class RoomRepositoryImpl implements RoomRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public RoomRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
    @Override
    public List<RoomResponseDto> findRoomResponseDtosByUserId(final int userId) {
        return queryFactory
                .select(Projections.constructor(RoomResponseDto.class, room.id, room.roomName))
                .from(roomUser)
                .join(roomUser.room, room)
                .where(roomUser.user.id.eq(userId))
                .fetch();
    }

    @Override
    public void deleteRoomUserByUserId(final int userId, final long roomId) {
        queryFactory
                .delete(roomUser)
                .where(roomUser.user.id.eq(userId).and(roomUser.room.id.eq(roomId)))
                .execute();

    }
}
