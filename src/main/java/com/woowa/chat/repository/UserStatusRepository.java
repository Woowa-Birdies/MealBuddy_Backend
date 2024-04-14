package com.woowa.chat.repository;

import com.woowa.chat.domain.UserStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatusRepository extends MongoRepository<UserStatus, String>{
}
