package com.woowa.gather.service;

import com.woowa.common.domain.ResourceNotFoundException;
import com.woowa.gather.domain.Post;
import com.woowa.gather.domain.dto.PostDetailsResponseDto;
import com.woowa.gather.domain.dto.PostListResponse;
import com.woowa.gather.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostReadService {

    private final PostRepository postRepository;

    public Post getById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(postId, "Post"));

    }

    public PostDetailsResponseDto findPostDetailsByPostId(Long postId) {
        return postRepository.findPostDetailsByPostId(postId)
                .orElseThrow(() -> new ResourceNotFoundException(postId, "Post"));
    }

    public List<PostListResponse> findDuePosts(int withinDate) {
        return postRepository.findDuePosts(LocalDateTime.now().plusDays(withinDate));
    }

}
