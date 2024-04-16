package com.woowa.chat.repository;

import com.woowa.chat.domain.UserStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStatusRepository extends MongoRepository<UserStatus, String>{
    List<UserStatus> findByRoomId(Long roomId);

    void deleteByUserIdAndRoomId(Long userId, Long roomId);
}
