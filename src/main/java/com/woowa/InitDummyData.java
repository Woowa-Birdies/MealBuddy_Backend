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

        createPost(user1);
        createPost(user1);
        createPost(user1);
        createPost(user2);
        createPost(user2);
        createPost(user2);
        createPost(user2);
    }

    private User createUser(String nickname) {
        return userRepository.save(new User(nickname));
    }

    private void createPost(User writer) {
        Location location = Location.builder()
                .address("경기도 화성시 ~")
                .place("ㅇㅇ떡볶이")
                .latitude(1.123)
                .longitude(1.123)
                .build();

        postRepository.save(Post.builder()
                .user(writer)
                .location(location)
                .contents("같이 밥먹을 파티원 구함")
                .ageTag(Age.values()[getRandNum(Age.values().length)])
                .foodTypeTag(FoodType.values()[getRandNum(FoodType.values().length)])
                .genderTag(Gender.FEMALE)
                .participantCount(1)
                .participantTotal(4)
                .postStatus(PostStatus.ONGOING)
                .meetAt(LocalDateTime.of(2024, 4, 20, 12, 30))
                .closeAt(LocalDateTime.of(2024, 4, 20, 11, 0))
                .build());
    }

    private int getRandNum(int limit) {
        return (int) (Math.random() * limit);
    }
}


