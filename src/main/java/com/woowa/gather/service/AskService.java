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
     * @param askRequest userId, postId
     * @return askResponse - askUserId, askId, postId, askStatus
     */
    public AskResponse saveAsk(AskRequest askRequest) {
        User foundUser = userRepository.findById(askRequest.getUserId())
                .orElseThrow(() -> new AskException(AskErrorCode.USER_NOT_FOUND));

//        if (foundUser.getBirthDate() == null) {
//            throw new AskException(AskErrorCode.UNVERIFIED_USER);
//        }

        Post foundPost = postRepository.findById(askRequest.getPostId())
                .orElseThrow(() -> new AskException(AskErrorCode.POST_NOT_FOUND));

        if (foundPost.getPostStatus() != PostStatus.ONGOING) {
            throw new AskException(AskErrorCode.ASK_DENIED);
        }

        log.info("postStatus : {}", foundPost.getPostStatus());

        askRepository.findByPostIdAndUserId(askRequest.getPostId(), askRequest.getUserId())
                .orElseThrow(() -> new AskException(AskErrorCode.ALREADY_ASKED_USER));

        Ask ask = askRepository.save(Ask.createAsk(foundPost, foundUser));

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
     * @param askId 신청 ID
     * @return askId
     */
    public Long deleteAsk(Long askId) {
        Ask ask = askRepository.findById(askId)
                .orElseThrow(() -> new AskException(AskErrorCode.ASK_NOT_FOUND));

        ask.getPost().removeAsk(ask);

        askRepository.delete(ask);

        return ask.getId();
    }

    public void deleteAsk(Long postId, Long userId) {
        Ask ask = askRepository.findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new AskException(AskErrorCode.ASK_NOT_FOUND));

        ask.getPost().removeAsk(ask);

        askRepository.delete(ask);
    }

    /**
     * 신청 상태 변경 (대기 -> 수락/거절 or 수락 -> 참여)
     *
     * @param askUpdate userId, postId, askId, askStatus(업데이트할 상태)
     * @return askResponse - askUserId, askId, postId, askStatus
     */
    public AskResponse changeAskStatus(AskUpdate askUpdate) {

        Post post = postRepository.findById(askUpdate.getPostId())
                .orElseThrow(() -> new AskException(AskErrorCode.POST_NOT_FOUND));

        Ask ask = askRepository.findById(askUpdate.getAskId())
                .orElseThrow(() -> new AskException(AskErrorCode.ASK_NOT_FOUND));

        ask.changeAskStatus(askUpdate.getAskStatus());

        return AskResponse.builder()
                .askUserId(askUpdate.getUserId())
                .askId(ask.getId())
                .postId(post.getId())
                .askStatus(ask.getAskStatus())
                .build();
    }

    public void participate(Long postId, Long userId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AskException(AskErrorCode.POST_NOT_FOUND));

        // 인원 찬 경우
        if (askRepository.countParticipantCountByPostId(post) == post.getParticipantTotal()) {
            throw new AskException(AskErrorCode.PARTICIPATION_DENIED);
        }

        Ask ask = askRepository.findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new AskException(AskErrorCode.ASK_NOT_FOUND));

        if (ask.getAskStatus() == AskStatus.PARTICIPATION) {
            throw new AskException(AskErrorCode.ALREADY_PARTICIPATED_USER);
        }

        ask.changeAskStatus(AskStatus.PARTICIPATION);

        if (askRepository.countParticipantCountByPostId(post) == post.getParticipantTotal()) {
            post.updatePostStatus(PostStatus.COMPLETION);
        }
    }

    /**
     * 모집글 신청자 리스트 조회
     *
     * @param postId 게시글 ID
     * @return List of PostAskListResponse - userId, askStatus, gender, age, introduce
     */
    public List<PostAskListResponse> getPostAskList(Long postId, int type) {

        return askRepository.findAskedUserByPostId(postId, getAskStatus(type));
    }

    /**
     * 유저가 작성한 게시글 리스트 조회
     *
     * @param userId 유저 ID
     * @param type   게시글 상태
     * @return List of PostListResponse
     */
    public List<PostListResponse> getUserPostList(Long userId, int type) {
        PostStatus postStatus = getPostStatus(type);

        return postRepository.findPostListByWriterId(userId, postStatus);
    }

    /**
     * 유저의 신청 리스트 조회
     *
     * @param userId 유저 ID
     * @param type   게시글 상태
     * @return List of AskListResponse
     */
    public List<AskListResponse> getAskList(Long userId, int type) {
        PostStatus postStatus = getPostStatus(type);
        return type == 0 ?
                askRepository.findWaitingOrRejectedAskList(userId) :
                askRepository.findUserAskListByWriterId(userId, getAskStatus(type));
    }

    private static AskStatus getAskStatus(int type) {
        return type == 0 ? AskStatus.WAITING : type == 1 ? AskStatus.ACCEPTED : AskStatus.PARTICIPATION;
    }

    private static PostStatus getPostStatus(int type) {
        return type == 0 ? PostStatus.ONGOING : type == 1 ? PostStatus.COMPLETION : PostStatus.CLOSED;
    }
}
