package com.woowa.room.repository;

import com.woowa.room.domain.Room;
import com.woowa.room.repository.support.RoomRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, RoomRepositoryCustom {

}
