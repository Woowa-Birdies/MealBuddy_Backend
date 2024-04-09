package com.woowa.room.service;

import com.woowa.gather.domain.Post;
import com.woowa.gather.repository.PostRepository;
import com.woowa.room.domain.Room;
import com.woowa.room.domain.dto.RoomResponseDto;
import com.woowa.room.repository.RoomRepository;
import com.woowa.room.repository.RoomUserRepository;
import com.woowa.user.domain.User;
import com.woowa.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomUserRepository roomUserRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 채팅방 불러오기
    public List<RoomResponseDto> getRoomInfo(final long userId) {
        log.info("getRoomInfo() userId: {}", userId);

        return roomUserRepository.getRoomInfo(userId);
    }

    // 채팅방 생성
    public RoomResponseDto createRoom(final long userId, final long postId) {
        log.info("createRoom() userId: {}, postId: {}", userId, postId);

        Post post = postRepository.findById(postId).orElseThrow(() -> {
            log.error("createRoom() post not found postId: {}", postId);
            //todo : exception handling "RoomEx002 post를 찾을 수 없습니다."
            return new IllegalArgumentException("post not found");
        });

        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("createRoom() user not found userId: {}", userId);
            //todo : exception handling "RoomEx001 사용자를 찾을 수 없습니다."
            return new IllegalArgumentException("user not found");
        });

        Room createdRoom = roomRepository.save(Room.builder()
                .user(user)
                .post(post)
                .roomName(post.getLocation().getPlace()+ " " + post.getMeetAt().toLocalDate().toString())
                .build());
        return RoomResponseDto.builder()
                .roomId(createdRoom.getId())
                .roomName(createdRoom.getRoomName())
                .build();
    }

}
