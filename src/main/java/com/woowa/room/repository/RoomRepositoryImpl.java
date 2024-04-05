package com.woowa.room.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woowa.room.domain.dto.RoomResponseDto;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.woowa.room.domain.QRoom.room;
import static com.woowa.room.domain.QRoomUser.roomUser;

public class RoomRepositoryImpl implements RoomRepositoryCustom{

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public RoomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(this.entityManager);
    }

    public List<RoomResponseDto> findRoomResponseDtosByUserId(int userId) {
        return queryFactory
                .select(Projections.constructor(RoomResponseDto.class, room.id, room.roomName))
                .from(roomUser)
                .join(roomUser.room, room)
                .where(roomUser.user.id.eq(userId))
                .fetch();
    }
}
