package com.woowa.gather.service;

import com.woowa.gather.domain.Post;
import com.woowa.gather.domain.dto.PostCreateDto;
import com.woowa.gather.repository.LocationRepository;
import com.woowa.gather.repository.PostRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Builder
@Transactional
@RequiredArgsConstructor
public class PostQueryService {
    private final PostRepository postRepository;
    private final LocationRepository locationRepository;

    public Long create(PostCreateDto postCreate) {
//        User user = userRepository
//                .findById(postCreate.getUserId())
//                .orElseThrow(() -> new NoSuchElementException("해당 Id에 해당하는 User를 찾을 수 없습니다."));
        Post post = postRepository
                .save(Post.toEntity(postCreate, user, UUID.randomUUID().toString()));

        return post.getId();
    }
}