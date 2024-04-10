package com.woowa.gather.service;

import com.woowa.common.domain.ResourceNotFoundException;
import com.woowa.gather.domain.Ask;
import com.woowa.gather.domain.Location;
import com.woowa.gather.domain.Post;
import com.woowa.gather.domain.dto.PostCreateDto;
import com.woowa.gather.domain.dto.PostUpdateDto;
import com.woowa.gather.repository.AskRepository;
import com.woowa.gather.repository.LocationRepository;
import com.woowa.gather.repository.PostRepository;
import com.woowa.user.domain.User;
import com.woowa.user.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Builder
@Transactional
@RequiredArgsConstructor
public class PostQueryService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final AskRepository askRepository;
    private final LocationRepository locationRepository;
    private final PostReadService postReadService;

    public Long create(PostCreateDto postCreateDto) {
        User user = userRepository
                .findById(postCreateDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(postCreateDto.getUserId(), "User"));

        // Location 엔티티 생성 및 저장
        Location savedLocation = locationRepository.save(
                Location.builder()
                        .place(postCreateDto.getPlace())
                        .address(postCreateDto.getAddress())
                        .build()
        );

        // Post 엔티티 생성 및 저장
        Post post = postRepository.save(
                Post.builder()
                        .user(user)
                        .location(savedLocation)
                        .participantTotal(postCreateDto.getParticipantTotal())
                        .participantCount(postCreateDto.getParticipantCount())
                        .contents(postCreateDto.getContents())
                        .postStatus(postCreateDto.getPostStatus())
                        .foodTypeTag(postCreateDto.getFoodTypeTag())
                        .ageTag(postCreateDto.getAgeTag())
                        .genderTag(postCreateDto.getGenderTag())
                        .meetAt(postCreateDto.getMeetAt())
                        .closeAt(postCreateDto.getCloseAt())
                        .build()
        );

        return post.getId();
    }

    public Long update(PostUpdateDto postUpdateDto) {
        Post post = postReadService.getById(postUpdateDto.getPostId());
        Location location = post.getLocation();

        location.update(
                Location.builder()
                        .place(postUpdateDto.getPlace())
                        .address(postUpdateDto.getAddress())
                        .build()
        );
        post.update(postUpdateDto);

        return post.getId();
    }

    public Long delete(Long postId) {
        Post post = postReadService.getById(postId);
        List<Ask> asks = post.getAsks();
        Location location = post.getLocation();

        // Ask 엔티티 삭제
        for (Ask ask : asks) {
            askRepository.delete(ask);
        }
        // Location 엔티티 삭제
        locationRepository.delete(location);
        postRepository.deleteById(postId);

        return post.getId();
    }

}