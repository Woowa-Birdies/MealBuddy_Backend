package com.woowa.gather.service;

import com.woowa.common.domain.ResourceNotFoundException;
import com.woowa.gather.domain.Ask;
import com.woowa.gather.domain.Post;
import com.woowa.gather.domain.dto.*;
import com.woowa.gather.domain.enums.AskStatus;
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

    public AskResponse saveAsk(AskRequest askRequest) {
        Post foundPost = postRepository.findById(askRequest.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException(askRequest.getPostId(), "포스트"));

        User foundUser = userRepository.findById(askRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(askRequest.getUserId(), "유저"));

        Ask ask = askRepository.save(Ask.createAsk(foundPost, foundUser));

        return AskResponse.builder()
                .askId(ask.getId())
                .askUserId(foundUser.getId())
                .postId(foundPost.getId())
                .askStatus(ask.getAskStatus())
                .build();
    }

    public Long deleteAsk(Long askId) {
        Ask ask = askRepository.findById(askId)
                .orElseThrow(() -> new ResourceNotFoundException(askId, "신청 내용"));

        askRepository.deleteById(askId);

        return ask.getId();
    }

    public AskResponse changeAskStatus(AskUpdate askUpdate) {
        Post post = postRepository.findById(askUpdate.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException(askUpdate.getPostId(), "게시글"));

        Ask ask = askRepository.findById(askUpdate.getAskId())
                .orElseThrow(() -> new ResourceNotFoundException(askUpdate.getAskId(), "신청 내용"));

        ask.changeAskStatus(askUpdate.getAskStatus());

        return AskResponse.builder()
                .askUserId(askUpdate.getUserId())
                .askId(ask.getId())
                .postId(post.getId())
                .askStatus(ask.getAskStatus())
                .build();
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
