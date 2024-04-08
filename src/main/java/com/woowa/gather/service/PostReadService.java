package com.woowa.gather.service;

import com.woowa.gather.domain.Post;
import com.woowa.gather.domain.dto.PostDetails;
import com.woowa.gather.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostReadService {

    private final PostRepository postRepository;

    public Post getById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found with id: " + postId));
    }

    public PostDetails findPostDetailsByPostId(Long postId) {
        return postRepository.findPostDetailsByPostId(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found with id: " + postId));
    }

}
