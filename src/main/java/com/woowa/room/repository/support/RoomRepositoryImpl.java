package com.woowa.room.repository.support;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woowa.gather.domain.enums.AskStatus;
import com.woowa.room.domain.dto.RoomResponseDto;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.woowa.gather.domain.QAsk.ask;
import static com.woowa.gather.domain.QPost.post;
import static com.woowa.room.domain.QRoom.room;
import static com.woowa.room.domain.QRoomUser.roomUser;
import static com.woowa.user.domain.QUser.user;

public class RoomRepositoryImpl implements RoomRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public RoomRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
    @Override
    public List<RoomResponseDto> findRoomResponseDtosByUserId(final long userId) {
        return queryFactory
                .select(Projections.constructor(RoomResponseDto.class, room.id, room.roomName))
                .from(roomUser)
                .join(roomUser.room, room)
                .where(roomUser.user.id.eq(userId))
                .fetch();
    }

    @Override
    public void deleteRoomUserByUserId(final long userId, final long roomId) {
        queryFactory
                .delete(roomUser)
                .where(roomUser.user.id.eq(userId).and(roomUser.room.id.eq(roomId)))
                .execute();

    }
    @Override
    public boolean isJoinable(final long  userId, final long postId) {
        return queryFactory
                .selectFrom(room)
                .leftJoin(room.post, post)
                .leftJoin(post, ask.post)
                .where(
                        ask.post.id.eq(postId).and(
                        ask.askStatus.eq(AskStatus.ACCEPTED)
                        ).and(user.id.eq(userId))
                )
                .fetchOne() != null;
    }
}
