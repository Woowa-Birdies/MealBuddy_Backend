package com.woowa.gather;

import com.woowa.gather.domain.Location;
import com.woowa.gather.domain.Post;
import com.woowa.gather.domain.dto.UserPostListResponse;
import com.woowa.gather.domain.enums.Age;
import com.woowa.gather.domain.enums.FoodType;
import com.woowa.gather.domain.enums.Gender;
import com.woowa.gather.domain.enums.PostStatus;
import com.woowa.gather.repository.AskRepository;
import com.woowa.gather.repository.PostRepository;
import com.woowa.user.domain.User;
import com.woowa.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class AskServiceTest {

    @Autowired
    private AskRepository askRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    void before() {
        User writer = createUser("nickname");
        createPost(writer);
        createPost(writer);
    }

    @Test
    @DisplayName(value = "사용자id로_모집_리스트를_가져올수있다")
    void getUserPostListByUserId() {

        List<UserPostListResponse> postListByWriterId = postRepository.findPostListByWriterId(1L, PostStatus.ONGOING).get();

//        assertThat(postListByWriterId.size()).isEqualTo(2);
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
