package com.woowa.room.repository.support;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woowa.gather.domain.Post;
import com.woowa.gather.domain.enums.AskStatus;
import com.woowa.room.domain.dto.RoomResponseDto;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

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
    public boolean deleteRoomUserByUserId(final long userId, final long roomId) {
        return queryFactory
                .delete(room)
                .where(room.user.id.eq(userId)
                        .and(room.id.eq(roomId)))
                .execute() > 0;
    }
    //todo: post domain 이동
    @Override
    public Optional<Post> findJoinablePostByPostId(final long  userId, final long postId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(post)
                .leftJoin(post.asks, ask)
                .where(
                        post.id.eq(postId)
                                .and(
                                        post.user.id.eq(userId)
                                                .or(
                                                        ask.askStatus.eq(AskStatus.ACCEPTED)
                                                                .and(ask.user.id.eq(userId))
                                                                .and(ask.post.id.eq(postId))
                                                )
                                )
                )
                .fetchOne());
    }
    //todo: post domain 이동
    @Override
    public void decreasePostCount(final long userId, final long roomId) {
        long postId = Optional.ofNullable(queryFactory
                .select(room.post.id)
                .from(room)
                .where(room.id.eq(roomId))
                .fetchOne()).orElseThrow(()->new IllegalArgumentException("post not found"));
        // 반영된 row가 없으면 에러
         queryFactory
            .update(post)
            .set(post.participantCount, post.participantCount.subtract(1))
            .where(
                    post.id.eq(postId)
                    .and(post.participantCount.gt(0))
                    .and(post.user.id.ne(userId))
            )
            .execute();
    }

    @Override
    public long postIdByRoomId(final long roomId) {
        return Optional.ofNullable(queryFactory
                .select(room.post.id)
                .from(room)
                .where(room.id.eq(roomId))
                .fetchOne()).orElseThrow();
    }
}
