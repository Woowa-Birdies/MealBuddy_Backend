package com.woowa.gather.service;

import com.woowa.common.domain.ResourceNotFoundException;
import com.woowa.gather.domain.Post;
import com.woowa.gather.domain.dto.PostDetailsResponseDto;
import com.woowa.gather.domain.dto.PostListResponse;
import com.woowa.gather.domain.enums.Age;
import com.woowa.gather.domain.enums.FoodType;
import com.woowa.gather.domain.enums.Gender;
import com.woowa.gather.domain.enums.PostStatus;
import com.woowa.gather.repository.PostRepository;
import com.woowa.gather.repository.PostSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostReadService {

    private final PostRepository postRepository;

    public Post getById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(postId, "Post"));

    }

//    public PostDetailsResponseDto findPostDetailsByPostId(Long postId) {
//        return postRepository.findPostDetailsByPostId(postId)
//                .orElseThrow(() -> new ResourceNotFoundException(postId, "Post"));
//    }

    public PostDetailsResponseDto findPostDetailsByPostIdAndUserId(Long postId, Long userId) {
        return postRepository.findPostDetailsByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(postId, "Post"));
    }

    public List<PostListResponse> findDuePosts(int withinDate) {
        return postRepository.findDuePosts(LocalDateTime.now().plusDays(withinDate));
    }

    public List<Post> filterPosts(List<Integer> dateTypes, List<FoodType> foodTypes, List<Age> ages, List<Gender> genders) {
        // 기본 Specification : ONGOING 상태
        Specification<Post> baseSpec = PostSpecification.findPostsEqualPostStatus(PostStatus.ONGOING);

        Specification<Post> resultDateSpec = combineDateSpecs(dateTypes); // 모임 날짜 Specification
        Specification<Post> resultFoodTypeSpec = combineTagSpecs(foodTypes, PostSpecification::findPostsEqualFoodType); // 냠냠 유형 Specification
        Specification<Post> resultAgeSpec = combineTagSpecs(ages, PostSpecification::findPostsEqualAge); // 연령대 Specification
        Specification<Post> resultGenderSpec = combineTagSpecs(genders, PostSpecification::findPostsEqualGender); // 성별 Specification

        Specification<Post> resultSpec = baseSpec;
        if (resultDateSpec != null) resultSpec = resultSpec.and(resultDateSpec);
        if (resultFoodTypeSpec != null) resultSpec = resultSpec.and(resultFoodTypeSpec);
        if (resultAgeSpec != null) resultSpec = resultSpec.and(resultAgeSpec);
        if (resultGenderSpec != null) resultSpec = resultSpec.and(resultGenderSpec);

        return postRepository.findAll(resultSpec);
    }

    // 각 Specification을 or로 결합
    private <T> Specification<Post> combineTagSpecs(List<T> filters, Function<T, Specification<Post>> specFunction) {
        return filters == null ? null : filters.stream()
                .map(specFunction)
                .reduce(Specification::or)
                .orElse(null);
    }

    private Specification<Post> combineDateSpecs(List<Integer> dateTypes) {
        if (dateTypes == null || dateTypes.isEmpty()) {
            return null;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = null;
        LocalDateTime end = null;

        List<Specification<Post>> specs = new ArrayList<>();

        // dateType 0: 오늘 / 1: 내일 / 2: 이번 주말
        for (Integer dateType : dateTypes) {
            switch (dateType) {
                case 0:
                    start = now.with(LocalTime.MIN); // Today 00:00:00
                    end = now.with(LocalTime.MAX); // Today 23:59:59
                    break;
                case 1:
                    start = now.plusDays(1).with(LocalTime.MIN);
                    end = now.plusDays(1).with(LocalTime.MAX);
                    break;
                case 2:
                    start = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY)).with(LocalTime.MIN); // This Saturday 00:00:00
                    end = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).with(LocalTime.MAX); // This Sunday 23:59:59
                    break;
                default:
                    continue;
            }

            if (start != null && end != null) {
                Specification<Post> spec = PostSpecification.findPostsBetweenDates(start, end);
                specs.add(spec);
            }
        }

        return specs.stream()
                .reduce(Specification::or)
                .orElse(null);
    }

}
