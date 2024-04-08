package com.woowa.gather.service;

import com.woowa.common.domain.ResourceNotFoundException;
import com.woowa.gather.domain.Location;
import com.woowa.gather.domain.Post;
import com.woowa.gather.domain.dto.PostCreateDto;
import com.woowa.gather.domain.dto.PostUpdateDto;
import com.woowa.gather.repository.LocationRepository;
import com.woowa.gather.repository.PostRepository;
import com.woowa.user.domain.User;
import com.woowa.user.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Builder
@Transactional
@RequiredArgsConstructor
public class PostQueryService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
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
                        .latitude(postCreateDto.getLatitude())
                        .longitude(postCreateDto.getLongitude())
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
                        .latitude(postUpdateDto.getLatitude())
                        .longitude(postUpdateDto.getLongitude())
                        .address(postUpdateDto.getAddress())
                        .build()
        );
        post.update(postUpdateDto);

        return post.getId();
    }

    public Long delete(Long postId) {
        Post post = postReadService.getById(postId);
        Location location = post.getLocation();

        locationRepository.delete(location); // OneToOne Join Location 테이블의 컬럼 삭제
        postRepository.deleteById(postId);

        return post.getId();
    }

}