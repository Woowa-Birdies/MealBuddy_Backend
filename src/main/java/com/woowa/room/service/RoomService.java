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
import jakarta.transaction.Transactional;
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
    @Transactional
    public RoomResponseDto createRoomInfo(final long userId, final long postId) {
        log.info("createRoom() userId: {}, postId: {}", userId, postId);

        Post post = postRepository.findById(postId).orElseThrow(() -> {
            log.error("createRoom() post not found postId: {}", postId);
            //todo : exception handling "RoomEx002 post를 찾을 수 없습니다.
            return new IllegalArgumentException("post not found");
        });

        //room 생성
        Room createdRoom = getCreatedRoom(userId, post);

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
    @Transactional
    public RoomResponseDto joinRoom(final long userId, final long postId){
        log.info("joinRoom() userId: {}, postId: {}", userId, postId);
        Post post = roomRepository.findJoinablePostByPostId(userId, postId).orElseThrow(()->{
            log.warn("joinRoom() post not found postId: {}", postId);
            //todo : exception handling "RoomEx004 참가할 수 없는 방입니다.
            return new IllegalArgumentException("post not found");
        });

        //참가자 수 증가 방장인 경우는 증가하지않음
        if(post.getUser().getId()!=userId) {
            post.addParticipantCount();
            postRepository.save(post);
        }

        //roomUser 저장 room이 없으면 생성
        RoomUser savedRoomUser = roomUserRepository.save(RoomUser.builder()
                .user(getUser(userId))
                .room(createRoomIfNotExists(userId, post))
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
    @Transactional
    public void leaveRoom(final long userId, final long roomId){
        log.info("leaveRoom() userId: {}, roomId: {}", userId, roomId);

        if(roomUserRepository.leaveRoom(userId, roomId)){
            log.error("leaveRoom() roomUser not found userId: {}, roomId: {}", userId, roomId);
            //todo : exception handling "RoomEx003 방을 찾을 수 없습니다.
            throw new IllegalArgumentException("roomUser not found");
        }

        roomRepository.decreasePostCount(userId, roomId);

    }

    /* 유저 강퇴
        * @param userId : 사용자(방장)
        * @param roomId : 채팅방
        * @param targetUserId : 강퇴 대상
     */
    @Transactional
    public void kickUser(final long userId, final long roomId, final long targetUserId){
        log.info("kickUser() userId: {}, roomId: {}, targetUserId: {}", userId, roomId, targetUserId);

        if(roomUserRepository.kickUser(userId, roomId, targetUserId)){
            log.error("kickUser() roomUser not found userId: {}, roomId: {}, targetUserId: {}", userId, roomId, targetUserId);
            //todo : exception handling "RoomEx007 해당하는 채팅방 유저를 찾을 수 없습니다.
            throw new IllegalArgumentException("roomUser not found");
        }
    }

    /* 채팅방 삭제
        * @param userId : 사용자(방장)
        * @param roomId : 채팅방
     */
    @Transactional
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



    private Room getCreatedRoom(long userId, Post post) {
       return roomRepository.save(Room.builder()
                .user(getUser(userId))
                .post(post)
                .roomName(post.getLocation().getPlace()+ " " + post.getMeetAt().toLocalDate().toString())
                .build());
    }

    private Room createRoomIfNotExists(final long postId, Post post) {
        return roomRepository.findByPostId(postId)
                .orElseGet(() -> getCreatedRoom(post.getUser().getId(), post));
    }

    private User getUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.error("getUser() user not found userId: {}", userId);
            //todo : exception handling "RoomEx001 사용자를 찾을 수 없습니다.
            return new IllegalArgumentException("user not found");
        });
    }

}
