package com.woowa.room.repository;

import com.woowa.room.domain.RoomUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomUserRepository extends JpaRepository<RoomUser, Long>, RoomUserRepositoryCustom{
}
