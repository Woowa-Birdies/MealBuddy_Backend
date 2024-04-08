package com.woowa.gather.service;

import com.woowa.common.domain.ResourceNotFoundException;
import com.woowa.gather.domain.dto.AskListResponse;
import com.woowa.gather.domain.dto.AskRequest;
import com.woowa.gather.domain.dto.UserPostListResponse;
import com.woowa.gather.domain.enums.FoodType;
import com.woowa.gather.domain.enums.PostStatus;
import com.woowa.gather.exception.NonExistTypeException;
import com.woowa.gather.repository.AskRepository;
import com.woowa.gather.repository.PostRepository;
import com.woowa.user.domain.User;
import com.woowa.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AskService {
    private final AskRepository askRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public int ask(AskRequest askRequest) {
        return 1;
    }

    public List<UserPostListResponse> getUserPostList(Long userId, int type) {PostStatus postStatus = type == 0 ? PostStatus.ONGOING : type == 1 ? PostStatus.COMPLETION : PostStatus.CLOSED;

        return postRepository.findPostListByWriterId(userId, postStatus)
                .orElseThrow(() -> new ResourceNotFoundException(userId, postStatus.getValue() + "리스트"));
    }
}
