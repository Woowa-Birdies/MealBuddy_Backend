package com.woowa.room.repository.support;

import com.woowa.gather.domain.Post;
import com.woowa.room.domain.dto.RoomResponseDto;

import java.util.List;
import java.util.Optional;

public interface RoomRepositoryCustom {
    List<RoomResponseDto> findRoomResponseDtosByUserId(long userId);
    boolean deleteRoomUserByUserId(long userId, long roomId);
    //room 참가 가능 여부
    Optional<Post> findJoinablePostByPostId(long userId, long postId);
    //post count 감소 todo: post로 이동
    void decreasePostCount(long userId, long postId);

    long postIdByRoomId(long roomId);

}
