package com.woowa.room.service;

import com.woowa.gather.domain.Post;
import com.woowa.gather.repository.PostRepository;
import com.woowa.room.domain.Room;
import com.woowa.room.domain.RoomUser;
import com.woowa.room.domain.dto.RoomResponseDto;
import com.woowa.room.event.RoomJoinEvent;
import com.woowa.room.event.RoomKickEvent;
import com.woowa.room.event.RoomLeaveEvent;
import com.woowa.room.exception.CustomRoomException;
import com.woowa.room.exception.RoomErrorCode;
import com.woowa.room.repository.RoomRepository;
import com.woowa.room.repository.RoomUserRepository;
import com.woowa.user.domain.User;
import com.woowa.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.DocFlavor;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.messaging.simp.SimpMessageHeaderAccessor.getUser;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomUserRepository roomUserRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

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
            return new CustomRoomException(RoomErrorCode.POST_NOT_FOUND);
        });

        //room 생성
        Room createdRoom = getCreatedRoom(userId, post);

        return RoomResponseDto.builder()
                .roomId(createdRoom.getId())
                .roomName(createdRoom.getRoomName())
                .postId(postId)
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
            return new CustomRoomException(RoomErrorCode.UNABLE_TO_JOIN);
        });

        //참가자 수 증가 방장인 경우는 증가하지않음
        increaseParticipation(userId, post);

        User user = getUser(userId);
        Room room = createRoomIfNotExists(postId, post);
        //roomUser에 같은 room, user가 있는지 확인
        if(roomUserRepository.existsByRoomIdAndUserId(room.getId(), userId)){
            log.error("joinRoom() roomUser already exists userId: {}, roomId: {}", userId, room.getId());
            throw new CustomRoomException(RoomErrorCode.ALREADY_JOINED);
        }
        //roomUser 저장 room이 없으면 생성
        RoomUser savedRoomUser = roomUserRepository.save(RoomUser.builder()
                .user(user)
                .room(room)
                .build());

        applicationEventPublisher.publishEvent(new RoomJoinEvent(savedRoomUser.getRoom().getId(), userId, post.getId(), savedRoomUser.getUser().getNickname()));

        return RoomResponseDto.builder()
                .roomId(savedRoomUser.getRoom().getId())
                .roomName(savedRoomUser.getRoom().getRoomName())
                .postId(postId)
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
            throw new CustomRoomException(RoomErrorCode.ROOM_NOT_FOUND);
        }

        applicationEventPublisher.publishEvent(new RoomLeaveEvent(roomId, userId, roomRepository.postIdByRoomId(roomId), getUser(userId).getNickname()));

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
            throw new CustomRoomException(RoomErrorCode.UNABLE_TO_KICK);
        }

        applicationEventPublisher.publishEvent(new RoomKickEvent(roomId, targetUserId, roomRepository.postIdByRoomId(roomId), getUser(targetUserId).getNickname()));
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
            throw new CustomRoomException(RoomErrorCode.ROOM_NOT_FOUND);
        }
        //채팅방 참가 인원 삭제
        roomUserRepository.deleteRoom(roomId);
    }

    private void increaseParticipation(long userId, Post post) {
        if(post.getUser().getId()!= userId) {
            post.addParticipantCount();
            postRepository.save(post);
        }
    }

    private Room getCreatedRoom(long userId, Post post) {
        //SQLIntegrityConstraintViolationException -> CustomException
        try {
            return roomRepository.save(Room.builder()
                    .user(getUser(userId))
                    .post(post)
                    .roomName(post.getLocation().getPlace() + " " + post.getMeetAt().toLocalDate().toString())
                    .build());
        } catch (Exception e) {
            log.error("getCreatedRoom() room save failed userId: {}, postId: {}", userId, post.getId());
            throw new CustomRoomException(RoomErrorCode.ROOM_SAVE_FAILED);
        }
    }

    @Transactional
    public Room createRoomIfNotExists(final long postId, Post post) {
        synchronized (this) {
            Room existingRoom = roomRepository.findByPostId(postId).orElse(null);
            return Objects.requireNonNullElseGet(existingRoom, () -> getCreatedRoom(post.getUser().getId(), post));
        }
    }

    private User getUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.error("getUser() user not found userId: {}", userId);
            return new CustomRoomException(RoomErrorCode.USER_NOT_FOUND);
        });
    }

}
