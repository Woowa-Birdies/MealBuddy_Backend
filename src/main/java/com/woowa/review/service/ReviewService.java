package com.woowa.review.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowa.review.domain.Review;
import com.woowa.review.domain.dto.UserInfoDto;
import com.woowa.review.repository.ReviewRepository;
import com.woowa.room.domain.Room;
import com.woowa.room.domain.RoomUser;
import com.woowa.room.repository.RoomRepository;
import com.woowa.room.repository.RoomUserRepository;
import com.woowa.user.domain.User;
import com.woowa.user.domain.dto.UserDTO;
import com.woowa.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RoomRepository roomRepository;
    private final RoomUserRepository roomUserRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    @Autowired
    public ReviewService(ReviewRepository reviewRepository, RoomRepository roomRepository, RoomUserRepository roomUserRepository, UserRepository userRepository, ObjectMapper objectMapper) {
        this.reviewRepository = reviewRepository;
        this.roomRepository = roomRepository;
        this.roomUserRepository = roomUserRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

//    @Autowired
//    public ReviewService(ReviewRepository reviewRepository, RoomRepository roomRepository, RoomUserRepository roomUserRepository, UserRepository userRepository) {
//        this.reviewRepository = reviewRepository;
//        this.roomRepository = roomRepository;
//        this.roomUserRepository = roomUserRepository;
//        this.userRepository = userRepository;
//    }

    @Transactional
    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

    public Long getRoomId(Long postId) {
        log.info("postId : {}",postId);
        Optional<Room> result = roomRepository.findByPostId(postId);
        Long roomId = result.map(Room::getId).orElse(null);
        return roomId;
    }

    public List<Long> getUserIdList(Long roomId) {
        // roomId를 기준으로 채팅방에 속한 사용자 정보를 가져옴
        List<RoomUser> byId = roomUserRepository.findByRoomId(roomId);
        return byId.stream()
                .map(roomUser -> roomUser.getUser().getId())
                .collect(Collectors.toList());
    }

    public List<UserInfoDto> getUserInfoByUserId(List<Long> userIdList) {
        List<User> userInfoList = userRepository.findAllById(userIdList);
        List<UserInfoDto> userDtoList = new ArrayList<>();
        for(User user : userInfoList) {
          UserInfoDto dto = UserInfoDto.fromUser(user);
          userDtoList.add(dto);
        }
        return userDtoList;
    }

}
