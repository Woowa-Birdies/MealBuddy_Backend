package com.woowa.room.repository;

import com.woowa.room.domain.Room;
import com.woowa.room.repository.support.RoomRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, RoomRepositoryCustom {

    @Query("select r from Room r where r.post.id = :postId")
    Optional<Room> findByPostId(long postId);

}
