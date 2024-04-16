package com.woowa.gather.service;

import com.woowa.common.domain.ResourceNotFoundException;
import com.woowa.gather.domain.Ask;
import com.woowa.gather.domain.Post;
import com.woowa.gather.domain.dto.*;
import com.woowa.gather.domain.enums.AskStatus;
import com.woowa.gather.domain.enums.PostStatus;
import com.woowa.gather.exception.AskErrorCode;
import com.woowa.gather.exception.AskException;
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

    /**
     * 신청
     *
     * @param askRequest - userId, postId
     * @return askResponse - askUserId, askId, postId, askStatus
     */
    public AskResponse saveAsk(AskRequest askRequest) {
        Post foundPost = postRepository.findById(askRequest.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException(askRequest.getPostId(), "포스트"));

        User foundUser = userRepository.findById(askRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(askRequest.getUserId(), "유저"));

        Ask ask = askRepository.save(Ask.createAsk(foundPost, foundUser));

        log.info("count {}", foundPost.getParticipantCount());

        return AskResponse.builder()
                .askId(ask.getId())
                .askUserId(foundUser.getId())
                .postId(foundPost.getId())
                .askStatus(ask.getAskStatus())
                .build();
    }

    /**
     * 신청 취소(삭제)
     *
     * @param askId - 신청 ID
     * @return askId
     */
    public Long deleteAsk(Long askId) {
        Ask ask = askRepository.findById(askId)
                .orElseThrow(() -> new ResourceNotFoundException(askId, "신청 내용"));

        ask.getPost().removeAsk(ask);

        askRepository.deleteById(askId);

        return ask.getId();
    }

    /**
     * 신청 상태 변경 (대기 -> 수락/거절)
     *
     * @param askUpdate - userId, postId, askId, askStatus(업데이트할 상태)
     * @return askResponse - askUserId, askId, postId, askStatus
     */
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

    public AskResponse participate(AskUpdate askUpdate){
        Post post = postRepository.findById(askUpdate.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException(askUpdate.getPostId(), "게시글"));

        if (askRepository.countParticipantCountByPostId(post) == post.getParticipantTotal()) {
            throw new AskException(AskErrorCode.PARTICIPATION_DENIED);
        }

        Ask ask = askRepository.findById(askUpdate.getAskId())
                .orElseThrow(() -> new ResourceNotFoundException(askUpdate.getAskId(), "신청 내용"));

        ask.changeAskStatus(AskStatus.PARTICIPATION);

        if (askRepository.countParticipantCountByPostId(post) == post.getParticipantTotal()) {
            post.updatePostStatus(PostStatus.COMPLETION);
        }

        return AskResponse.builder()
                .askUserId(askUpdate.getUserId())
                .askId(ask.getId())
                .postId(post.getId())
                .askStatus(ask.getAskStatus())
                .build();
    }

    /**
     * 모집글 신청자 리스트 조회
     *
     * @param postId - 게시글 ID
     * @return List of PostAskListResponse - userId, askStatus, gender, age, introduce
     */
    public List<PostAskListResponse> getPostAskList(Long postId, int type) {
        if (type == 0) {
            return askRepository.findAskedUserByPostId(postId)
                    .orElseThrow(() -> new ResourceNotFoundException(postId, "신청자 리스트"));
        } else if (type == 1) {
            return askRepository.findAcceptedUserByPostId(postId)
                    .orElseThrow(() -> new ResourceNotFoundException(postId, "신청자 리스트"));
        } else {
            return askRepository.findParticipatedUserByPostId(postId)
                    .orElseThrow(() -> new ResourceNotFoundException(postId, "신청자 리스트"));
        }
    }

    /**
     * 유저가 작성한 게시글 리스트 조회
     *
     * @param userId - 유저 ID
     * @param type   - 게시글 상태
     * @return List of PostListResponse
     */
    public List<PostListResponse> getUserPostList(Long userId, int type) {
        PostStatus postStatus = getPostStatus(type);

        return postRepository.findPostListByWriterId(userId, postStatus)
                .orElseThrow(() -> new ResourceNotFoundException(userId, postStatus.getValue() + " 리스트"));
    }

    /**
     * 유저의 신청 리스트 조회
     *
     * @param userId - 유저 ID
     * @param type   - 게시글 상태
     * @return List of AskListResponse
     */
    public List<AskListResponse> getAskList(Long userId, int type) {
        PostStatus postStatus = getPostStatus(type);

        if (type == 0) {
            return askRepository.findWaitingOrRejectedAskList(userId)
                    .orElseThrow(() -> new ResourceNotFoundException(userId, postStatus.getValue() + "신청한 리스트"));
        } else if (type == 1) {
            return askRepository.findUserAskListByWriterId(userId, AskStatus.ACCEPTED)
                    .orElseThrow(() -> new ResourceNotFoundException(userId, postStatus.getValue() + "신청한 리스트"));
        } else {
            return askRepository.findUserAskListByWriterId(userId, AskStatus.PARTICIPATION)
                    .orElseThrow(() -> new ResourceNotFoundException(userId, postStatus.getValue() + "신청한 리스트"));
        }


    }

    private static PostStatus getPostStatus(int type) {
        return type == 0 ? PostStatus.ONGOING : type == 1 ? PostStatus.COMPLETION : PostStatus.CLOSED;
    }
}
