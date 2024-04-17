package com.woowa.chat.repository;

import com.woowa.chat.domain.Chat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {
    @Query("{ roomId: ?0, createAt: { $lt: ?1 } }")
    Slice<Chat> findChatsByRoomIdBefore(Long roomId, LocalDateTime date, Pageable pageable);

}
