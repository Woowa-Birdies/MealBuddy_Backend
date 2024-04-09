package com.woowa.room.service;

import com.woowa.gather.domain.Post;
import com.woowa.gather.repository.PostRepository;
import com.woowa.room.domain.Room;
import com.woowa.room.domain.RoomUser;
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

    /* 채팅방 불러오기
        * @param userId : 사용자
        * @return List<RoomResponseDto> : 사용자가 참여한 채팅방 정보
     */
    public List<RoomResponseDto> getRoomInfo(final long userId) {
        log.info("getRoomInfo() userId: {}", userId);

        return roomUserRepository.getRoomInfo(userId);
    }

    /* 채팅방 생성
        * @param userId : 사용자
        * @param postId : 게시글
        * @return RoomResponseDto : 생성된 채팅방 정보
     */
    public RoomResponseDto createRoom(final long userId, final long postId) {
        log.info("createRoom() userId: {}, postId: {}", userId, postId);

        Post post = postRepository.findById(postId).orElseThrow(() -> {
            log.error("createRoom() post not found postId: {}", postId);
            //todo : exception handling "RoomEx002 post를 찾을 수 없습니다.
            return new IllegalArgumentException("post not found");
        });

        Room createdRoom = roomRepository.save(Room.builder()
                .user(getUser(userId))
                .post(post)
                .roomName(post.getLocation().getPlace()+ " " + post.getMeetAt().toLocalDate().toString())
                .build());

        return RoomResponseDto.builder()
                .roomId(createdRoom.getId())
                .roomName(createdRoom.getRoomName())
                .build();
    }

    /* 채팅방 참가
        * @param userId : 사용자
        * @param postId : 게시글
        * @return RoomResponseDto : 참가한 채팅방 정보
     */
    public RoomResponseDto joinRoom(final long userId, final long postId){
        log.info("joinRoom() userId: {}, postId: {}", userId, postId);
        Post post = roomRepository.findJoinablePostByPostId(userId, postId).orElseThrow(()->{
            log.warn("joinRoom() post not found postId: {}", postId);
            //todo : exception handling "RoomEx004 참가할 수 없는 방입니다.
            return new IllegalArgumentException("post not found");
        });

        //참가자 수 증가
        post.addParticipantCount();
        postRepository.save(post);

        RoomUser savedRoomUser = roomUserRepository.save(RoomUser.builder()
                .user(getUser(userId))
                .room(roomRepository.findByPostId(postId).orElseThrow(() -> {
                    log.warn("joinRoom() room not found postId: {}", postId);
                    //todo : exception handling "RoomEx003 방을 찾을 수 없습니다.
                    return new IllegalArgumentException("room not found");
                }))
                .build());

        return RoomResponseDto.builder()
                .roomId(savedRoomUser.getRoom().getId())
                .roomName(savedRoomUser.getRoom().getRoomName())
                .build();
    }

    /* 채팅방 나가기
        * @param userId : 사용자
        * @param roomId : 채팅방
     */
    public void leaveRoom(final long userId, final long roomId){
        log.info("leaveRoom() userId: {}, roomId: {}", userId, roomId);

        if(roomUserRepository.leaveRoom(userId, roomId)){
            log.error("leaveRoom() roomUser not found userId: {}, roomId: {}", userId, roomId);
            //todo : exception handling "RoomEx003 방을 찾을 수 없습니다.
            throw new IllegalArgumentException("roomUser not found");
        }

        if(roomRepository.decreasePostCount(roomId) == 0){
            log.error("leaveRoom() post not found roomId: {}", roomId);
            //todo : exception handling "PostEx00? post를 찾을 수 없습니다.
            throw new IllegalArgumentException("post not found");
        }

    }

    /* 채팅방 삭제
        * @param userId : 사용자(방장)
        * @param roomId : 채팅방
     */
    public void deleteRoom(final long userId, final long roomId){
        log.info("deleteRoom() userId: {}, roomId: {}", userId, roomId);
        //채팅방 삭제
        if(roomRepository.deleteRoomUserByUserId(userId, roomId)){
            log.error("deleteRoom() roomUser not found userId: {}, roomId: {}", userId, roomId);
            //todo : exception handling "RoomEx003 방을 찾을 수 없습니다.
            throw new IllegalArgumentException("roomUser not found");
        }
        //채팅방 참가 인원 삭제
        roomUserRepository.deleteRoom(roomId);
    }

    private User getUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.error("getUser() user not found userId: {}", userId);
            //todo : exception handling "RoomEx001 사용자를 찾을 수 없습니다.
            return new IllegalArgumentException("user not found");
        });
    }

}
