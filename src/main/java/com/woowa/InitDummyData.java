package com.woowa;

import com.woowa.gather.domain.Ask;
import com.woowa.gather.domain.Location;
import com.woowa.gather.domain.Post;
import com.woowa.gather.domain.enums.Age;
import com.woowa.gather.domain.enums.FoodType;
import com.woowa.gather.domain.enums.Gender;
import com.woowa.gather.domain.enums.PostStatus;
import com.woowa.gather.repository.AskRepository;
import com.woowa.gather.repository.PostRepository;
import com.woowa.user.domain.User;
import com.woowa.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitDummyData {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final AskRepository askRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {
        String[] names = {"user1", "user2", "user3"};
        List<User> users = new ArrayList<>();
        List<Post> posts = new ArrayList<>();

        for (String name : names) {
            users.add(createUser(name));
        }

        for (User user : users) {
            posts.add(createOngoingPost(user));
            posts.add(createOngoingPost(user));
            posts.add(createOngoingPost(user));
            posts.add(createOngoingPost(user));
            posts.add(createOngoingPost(user));
            posts.add(createOngoingPost(user));
            posts.add(createOngoingPost(user));
            posts.add(createOngoingPost(user));
            posts.add(createCompletionPost(user));
            posts.add(createCompletionPost(user));
            posts.add(createCompletionPost(user));
            posts.add(createCompletionPost(user));
            posts.add(createCompletionPost(user));
            posts.add(createCompletionPost(user));
            posts.add(createCompletionPost(user));
            posts.add(createCompletionPost(user));
            posts.add(createCompletionPost(user));
            posts.add(createCompletionPost(user));
            posts.add(createClosedPost(user));
            posts.add(createClosedPost(user));
            posts.add(createClosedPost(user));
            posts.add(createClosedPost(user));
            posts.add(createClosedPost(user));
            posts.add(createClosedPost(user));
            posts.add(createClosedPost(user));
        }

        for (int i = 0; i < users.size(); i++) {
            int start = (i == 0 ? 25 : i == 1 ? 50 : 0);
            int end = (i == 0 ? 50 : i == 1 ? 75 : 25);
            for (int j = start; j < end; j ++){
                createAsk(users.get(i), posts.get(j));
            }
        }
    }

    private User createUser(String nickname) {
        return userRepository.save(new User(nickname));
    }

    private Post createOngoingPost(User writer) {
        return createPost(writer, PostStatus.ONGOING, 1, 5, 4);
    }

    private Post createCompletionPost(User writer) {
        return createPost(writer, PostStatus.COMPLETION, 4, 4, 4);
    }

    private Post createClosedPost(User writer) {
        return createPost(writer, PostStatus.CLOSED, 4, 4, 1);
    }

    private Post createPost(User writer, PostStatus postStatus, int pCount, int pTotal, int month) {
        Location location = Location.builder()
                .address("서울 광진구 자양로43길 42 1층")
                .place("신토불이떡볶이 본점")
                .latitude(37.552484)
                .longitude(127.090460)
                .build();

        int day = getRandNum2(3, 30);

        return postRepository.save(Post.builder()
                .user(writer)
                .location(location)
                .contents("같이 밥먹을 파티원 구합니다.")
                .ageTag(Age.values()[getRandNum1(Age.values().length)])
                .foodTypeTag(FoodType.values()[getRandNum1(FoodType.values().length)])
                .genderTag(Gender.values()[getRandNum1(Gender.values().length)])
                .participantCount(pCount)
                .participantTotal(pTotal)
                .postStatus(postStatus)
                .meetAt(LocalDateTime.of(2024, month, day, 12, 30))
                .closeAt(LocalDateTime.of(2024, month, day - 2, 11, 0))
                .build());
    }

    private void createAsk(User user, Post post) {
        askRepository.save(Ask.builder()
                .user(user)
                .post(post)
                .build());
    }

    private int getRandNum1(int limit) {
        return (int) (Math.random() * limit);
    } // 0 포함

    private int getRandNum2(int min, int max) {
        return (int) ((Math.random() * (max - min + 1)) + min);
    } // min ~ max
}
