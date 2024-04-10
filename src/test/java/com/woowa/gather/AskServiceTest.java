package com.woowa.gather;

import com.woowa.gather.domain.Ask;
import com.woowa.gather.domain.Location;
import com.woowa.gather.domain.Post;
import com.woowa.gather.domain.dto.*;
import com.woowa.gather.domain.enums.*;
import com.woowa.gather.repository.AskRepository;
import com.woowa.gather.repository.PostRepository;
import com.woowa.gather.service.AskService;
import com.woowa.user.domain.User;
import com.woowa.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class AskServiceTest {

    @Autowired
    private AskRepository askRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AskService askService;

    @BeforeAll
    void before() {
        User writer = createUser("writer");
        Post post1 = createPost(writer);
        Post post2 = createPost(writer);

        User participant = createUser("신청자");
    }

    @Test
    @DisplayName(value = "사용자id로_모집_리스트를_가져올수있다")
    void getUserPostListByUserId() {

        List<UserPostListResponse> postListByWriterId = postRepository.findPostListByWriterId(1L, PostStatus.ONGOING).get();

//        assertThat(postListByWriterId.size()).isEqualTo(2);
    }

    @Test
    @DisplayName(value = "사용자id로_신청_리스트를_가져올수있다")
    void getAskListByUserId() {

//        List<AskListResponse> askListByWriterId = askRepository.findAskListByWriterId(1L, PostStatus.ONGOING).get();
//
//        assertThat(askListByWriterId.size()).isEqualTo(0);
    }

    @Test
    @DisplayName(value = "신청하기")
    void saveAsk() {
        // 신청자 id : 4, 신청할 게시물 id : 8
//        AskRequest askRequest = AskRequest.builder()
//                .postId(8L)
//                .userId(4L)
//                .build();
        // assertThat(askService.saveAsk(askRequest)).isEqualTo(1L);
    }

    @Test
    @DisplayName(value = "신청 취소")
    void deleteAsk() {
//        AskRequest askRequest = AskRequest.builder()
//                .postId(8L)
//                .userId(4L)
//                .build();
//        Long askId = askService.saveAsk(askRequest);
//
//        Long deletedAskId = askService.deleteAsk(askId);
//
//        assertThat(deletedAskId).isEqualTo(askId);
    }

    @Test
    @DisplayName(value = "신청 상태 변경")
    void changeAskStatus() {
        User writer = userRepository.save(new User("writer"));
        Location location = Location.builder()
                .place("place")
                .address("address")
                .longitude(1.123)
                .latitude(1.123)
                .build();
        Post post = postRepository.save(Post.builder()
                .user(writer)
                .location(location)
                .contents("같이 밥먹을 파티원 구함")
                .ageTag(Age.AGE20S)
                .foodTypeTag(FoodType.ALCOHOL)
                .genderTag(Gender.FEMALE)
                .participantCount(1)
                .participantTotal(4)
                .postStatus(PostStatus.ONGOING)
                .meetAt(LocalDateTime.of(2024, 4, 20, 12,30))
                .closeAt(LocalDateTime.of(2024, 4, 20, 11,0))
                .build());
        User participant = userRepository.save(new User("participant"));
        Ask ask = askRepository.save(Ask.builder()
                        .user(participant)
                        .post(post)
                .build());

        log.info("original status = {}", ask.getAskStatus());

        AskUpdate askUpdate = AskUpdate.builder()
                .askId(ask.getId())
                .userId(participant.getId())
                .postId(post.getId())
                .askStatus(AskStatus.ACCEPTED)
                .build();
        AskResponse askResponse = askService.changeAskStatus(askUpdate);
        log.info("result status = {}", askResponse.getAskStatus());

        Assertions.assertThat(askUpdate.getAskStatus()).isEqualTo(askResponse.getAskStatus());
    }

    @Test
    @DisplayName(value = "신청자 리스트 가져오기")
    void getPostAskList() {
//        AskRequest askRequest = AskRequest.builder()
//                .postId(8L)
//                .userId(4L)
//                .build();

//        askService.saveAsk(askRequest);

//        assertThat(askService.getPostAskList(8L).size()).isEqualTo(1);
    }

    @Test
    @DisplayName(value = "findByUser")
    void findByUser() {
        User foundUser = userRepository.findById(1L).get();
        Post foundPost = postRepository.findById(51L).get();
//        assertThat(askRepository.findByUserAndPost(foundUser, foundPost).get().getUser()).isEqualTo(foundUser.getId());
//        Optional<Ask> result = askRepository.findByUserAndPost(foundUser, foundPost);
//        log.info("result = {}", result.get().getCreatedAt());

        boolean result = askRepository.existsAskByUserAndPost(foundUser, foundPost);
        log.info("result = {}", result);
    }

    private Post createPost(User writer) {
        Location location = Location.builder()
                .address("경기도 화성시 ~")
                .place("ㅇㅇ떡볶이")
                .longitude(1.1234)
                .latitude(1.1234)
                .build();
        return postRepository.save(Post.builder()
                .user(writer)
                .location(location)
                .contents("같이 밥먹을 파티원 구함")
                .ageTag(Age.AGE20S)
                .foodTypeTag(FoodType.ALCOHOL)
                .genderTag(Gender.FEMALE)
                .participantCount(1)
                .participantTotal(4)
                .postStatus(PostStatus.ONGOING)
                .meetAt(LocalDateTime.of(2024, 4, 20, 12,30))
                .closeAt(LocalDateTime.of(2024, 4, 20, 11,0))
                .build());
    }

    private User createUser(String nickname) {
        return userRepository.save(new User(nickname));
    }
}
