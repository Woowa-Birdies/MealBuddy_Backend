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
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

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

        List<Specification<Post>> dateSpec = new ArrayList<>(); // 모임 날짜 필터링 Specification
        List<Specification<Post>> foodTypeSpec = new ArrayList<>(); // 냠냠 유형 필터링 Specification

        // 모임 날짜 필터링
        if (dateTypes != null) {
            LocalDateTime startOfToday = LocalDate.now().atStartOfDay(); // Today 00:00:00
            LocalDateTime endOfToday = LocalDate.now().atTime(23, 59, 59); // Today 23:59:59
            LocalDateTime startOfThisSaturday = startOfToday.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY)); // This Saturday 00:00:00
            LocalDateTime endOfThisSunday = startOfThisSaturday.plusDays(1).withHour(23).withMinute(59).withSecond(59); // This Sunday 23:59:59

            // dateType [0: 오늘 / 1: 내일 / 2: 이번 주말]
            for (Integer dateType : dateTypes) {
                switch (dateType) {
                    case 0:
                        dateSpec.add(PostSpecification.findPostsBetweenDates(startOfToday, endOfToday));
                        break;
                    case 1:
                        dateSpec.add(PostSpecification.findPostsBetweenDates(startOfToday.plusDays(1), endOfToday.plusDays(1)));
                        break;
                    case 2:
                        dateSpec.add(PostSpecification.findPostsBetweenDates(startOfThisSaturday, endOfThisSunday));
                        break;
                    default:
                        continue;
                }
            }
        }

        // 냠냠 유형 필터링
        if (foodTypes != null) {
            for (FoodType foodType : foodTypes) {
                foodTypeSpec.add(PostSpecification.findPostsEqualFoodType(foodType));
            }
        }

        // dateSpec의 각 Specification을 or로 결합
        Specification<Post> dateCombinedSpec = dateSpec.stream()
                .reduce(Specification::or)
                .map(baseSpec::and)
                .orElse(baseSpec);

        // foodTypeSpec의 각 Specification을 or로 결합
        Specification<Post> foodTypeCombinedSpec = foodTypeSpec.stream()
                .reduce(Specification::or)
                .map(baseSpec::and)
                .orElse(baseSpec);

        // 최종 결과: (baseSpec and dateSpec) and (baseSpecfood and TypeSpecs)
        Specification<Post> resultSpec = dateCombinedSpec.and(foodTypeCombinedSpec);

        return postRepository.findAll(resultSpec);
    }

}
