package com.woowa.review.service;

import com.woowa.chat.domain.UserStatus;
import com.woowa.chat.repository.UserStatusRepository;
import com.woowa.review.domain.Review;
import com.woowa.review.repository.ReviewRepository;
import com.woowa.room.domain.Room;
import com.woowa.room.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RoomRepository roomRepository;
    private final UserStatusRepository userStatusRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, RoomRepository roomRepository, UserStatusRepository userStatusRepository) {
        this.reviewRepository = reviewRepository;
        this.roomRepository = roomRepository;
        this.userStatusRepository = userStatusRepository;
    }

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

    public List<Long> getUserIds(Long roomId) {
        log.info("ReviewService roomId = {}",roomId);

        // roomId를 기준으로 채팅방에 속한 사용자 정보를 가져옴
        List<UserStatus> userList = userStatusRepository.findByRoomId(roomId);

        return userList.stream()
                .map(UserStatus::getUserId)
                .collect(Collectors.toList());
    }

}
