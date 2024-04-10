package com.woowa.gather.service;

import com.woowa.common.domain.ResourceNotFoundException;
import com.woowa.gather.domain.Ask;
import com.woowa.gather.domain.Post;
import com.woowa.gather.domain.dto.AskListResponse;
import com.woowa.gather.domain.dto.AskRequest;
import com.woowa.gather.domain.dto.PostAskListResponse;
import com.woowa.gather.domain.dto.PostListResponse;
import com.woowa.gather.domain.enums.PostStatus;
import com.woowa.gather.repository.AskRepository;
import com.woowa.gather.repository.PostRepository;
import com.woowa.user.domain.User;
import com.woowa.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AskService {
    private final AskRepository askRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Long saveAsk(AskRequest askRequest) {
        Post foundPost = postRepository.findById(askRequest.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException(askRequest.getPostId(), "포스트"));

        User foundUser = userRepository.findById(askRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(askRequest.getUserId(), "유저"));

        Ask ask = askRepository.save(Ask.builder()
                .post(foundPost)
                .user(foundUser)
                .build());

        return ask.getId();
    }

    public Long deleteAsk(Long askId) {
        Ask ask = askRepository.findById(askId)
                .orElseThrow(() -> new ResourceNotFoundException(askId, "신청 내용"));

        // todo: 유저의 참여 상태가 수락/참여 상태인 경우 -> 참여count -1 | 대기/거절 상태인 경우 -> 그냥 삭제

        askRepository.deleteById(askId);

        return ask.getId();
    }

    public List<PostAskListResponse> getPostAskList(Long postId) {
        return askRepository.findAskedUserByPostId(postId)
                .orElseThrow(() -> new ResourceNotFoundException(postId, "신청자 리스트"));
    }

    public List<PostListResponse> getUserPostList(Long userId, int type) {
        PostStatus postStatus = getPostStatus(type);

        return postRepository.findPostListByWriterId(userId, postStatus)
                .orElseThrow(() -> new ResourceNotFoundException(userId, postStatus.getValue() + " 리스트"));
    }

    public List<AskListResponse> getAskList(Long userId, int type) {
        PostStatus postStatus = getPostStatus(type);

        return askRepository.findAskListByWriterId(userId, postStatus)
                .orElseThrow(() -> new ResourceNotFoundException(userId, postStatus.getValue() + " 리스트"));
    }

    private static PostStatus getPostStatus(int type) {
        return type == 0 ? PostStatus.ONGOING : type == 1 ? PostStatus.COMPLETION : PostStatus.CLOSED;
    }
}
