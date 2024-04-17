package com.woowa.room.repository;

import com.woowa.room.domain.RoomUser;
import com.woowa.room.repository.support.RoomUserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomUserRepository extends JpaRepository<RoomUser, Long>, RoomUserRepositoryCustom {
}
