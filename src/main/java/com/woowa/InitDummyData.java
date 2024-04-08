package com.woowa;

import com.woowa.gather.domain.Location;
import com.woowa.gather.domain.Post;
import com.woowa.gather.domain.enums.Age;
import com.woowa.gather.domain.enums.FoodType;
import com.woowa.gather.domain.enums.Gender;
import com.woowa.gather.domain.enums.PostStatus;
import com.woowa.gather.repository.PostRepository;
import com.woowa.user.domain.User;
import com.woowa.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InitDummyData {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {
        User user1 = createUser("user1");
        User user2 = createUser("user2");
        User user3 = createUser("user3");

        createPost(user1);
        createPost(user1);
        createPost(user2);
        createPost(user2);
        createPost(user3);
        createPost(user3);
        createPost(user3);
    }

    private User createUser(String nickname) {
        return userRepository.save(new User(nickname));
    }

    private void createPost(User writer) {
        Location location = Location.builder()
                .address("서울 광진구 자양로43길 42 1층")
                .place("신토불이떡볶이 본점")
                .latitude(37.552484)
                .longitude(127.090460)
                .build();

        postRepository.save(Post.builder()
                .user(writer)
                .location(location)
                .contents("같이 밥먹을 파티원 구합니다.")
                .ageTag(Age.values()[getRandNum1(Age.values().length)])
                .foodTypeTag(FoodType.values()[getRandNum1(FoodType.values().length)])
                .genderTag(Gender.values()[getRandNum1(Gender.values().length)])
                .participantCount(getRandNum2(0, 1))
                .participantTotal(getRandNum2(1, 5))
                .postStatus(PostStatus.ONGOING)
                .meetAt(LocalDateTime.of(2024, 4, getRandNum2(22, 30), 12, 30))
                .closeAt(LocalDateTime.of(2024, 4, getRandNum2(10, 21), 11, 0))
                .build());
    }

    private int getRandNum1(int limit) {
        return (int) (Math.random() * limit);
    } // 0 포함

    private int getRandNum2(int min, int max) {
        return (int) ((Math.random() * (max - min + 1)) + min);
    } // min ~ max
}


