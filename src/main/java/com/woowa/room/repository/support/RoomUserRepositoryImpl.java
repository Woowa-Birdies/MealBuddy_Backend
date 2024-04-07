package com.woowa.room.repository.support;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
@Slf4j
@Repository
public class RoomUserRepositoryImpl implements RoomUserRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public RoomUserRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }


}
