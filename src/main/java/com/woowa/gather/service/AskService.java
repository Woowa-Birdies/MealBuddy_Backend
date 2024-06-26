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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        log.info("user email {}, birthdate {}", foundUser.getEmail(), foundUser.getBirthDate());

//        if (foundUser.getBirthDate() == null) {
//            throw new AskException(AskErrorCode.UNVERIFIED_USER);
//        }

        Post foundPost = postRepository.findById(askRequest.getPostId())
                .orElseThrow(() -> new AskException(AskErrorCode.POST_NOT_FOUND));

        if (foundPost.getPostStatus() != PostStatus.ONGOING) {
            throw new AskException(AskErrorCode.ASK_DENIED);
        }

        log.info("postStatus : {}", foundPost.getPostStatus());

        Optional<Ask> byPostIdAndUserId = askRepository.findByPostIdAndUserId(askRequest.getPostId(), askRequest.getUserId());

        if (byPostIdAndUserId.isPresent()) {
            throw new AskException(AskErrorCode.ALREADY_ASKED_USER);
        }

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

        Ask ask = askRepository.findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new AskException(AskErrorCode.ASK_NOT_FOUND));

        if (ask.getAskStatus() != AskStatus.PARTICIPATION) {
//            throw new AskException(AskErrorCode.ALREADY_PARTICIPATED_USER);
            // post 있는건지 확인
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new AskException(AskErrorCode.POST_NOT_FOUND));

            // 이미 인원 찬 경우
            if (askRepository.countParticipantCountByPostId(post) == post.getParticipantTotal()) {
                throw new AskException(AskErrorCode.PARTICIPATION_DENIED);
            }

            ask.changeAskStatus(AskStatus.PARTICIPATION);

            if (askRepository.countParticipantCountByPostId(post) == post.getParticipantTotal()) {
                post.updatePostStatus(PostStatus.COMPLETION);
            }
        }
    }

    public boolean checkIfPostNotExistsByUser(Long postId, Long userId) {
        return postRepository.findByPostIdAndUserId(postId, userId) == 0;
    }

    /**
     * 모집글 신청자 리스트 조회
     *
     * @param postId 게시글 ID
     * @return List of PostAskListResponse - userId, askStatus, gender, age, introduce
     */
    public ListResponse<PostAskListResponse> getPostAskList(Long postId, int type, int page, int pageSize, int totalPages) {
        AskStatus askStatus = getAskStatus(type);

        int pages = askRepository.countAsksByPostIdAndAskStatus(postId, askStatus);
        if (pages % pageSize != 0) {
            pages = pages / pageSize + 1;
        } else {
            pages /= pageSize;
        }

        if (page == pages) {
            page = 0;
        }

        Page<PostAskListResponse> result = askRepository.findAskedUserByPostId(postId, askStatus,
                PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id")));

        return new ListResponse<>(result.toList(), result.getPageable().getPageNumber(), pageSize, result.getTotalElements(), result.getTotalPages());
    }

    /**
     * 유저가 작성한 게시글 리스트 조회
     *
     * @param userId 유저 ID
     * @param type   게시글 상태
     * @return List of PostListResponse
     */
    public Page<PostListResponse> getUserPostList(Long userId, int type, int page, int pageSize, int totalPages) {
        PostStatus postStatus = getPostStatus(type);

        int pages = postRepository.countPostsByUserIdAndPostStatus(userId, postStatus);
        if (pages % pageSize != 0) {
            pages = pages / pageSize + 1;
        } else {
            pages /= pageSize;
        }

        if (page == pages) {
            page = 0;
        }

        return postRepository.findPostListByWriterId(userId, postStatus,
                PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id")));
    }

    /**
     * 유저의 신청 리스트 조회
     *
     * @param userId 유저 ID
     * @param type   게시글 상태
     * @return List of AskListResponse
     */
    public Page<AskListResponse> getAskList(Long userId, int type, int page, int pageSize, int totalPages) {

        if (type == 0) {
            int pages = askRepository.countAsksByUserIdAndAndAskStatus(userId);
            if (pages % pageSize != 0) {
                pages = pages / pageSize + 1;
            } else {
                pages /= pageSize;
            }

            log.info("ask list pages = {}", pages);
            if (page == pages) {
                page = 0;
            }

            return askRepository.findWaitingOrRejectedAskList(
                    userId,
                    PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id")));
        } else {
            AskStatus askStatus = getAskStatus(type);
            int pages = askRepository.countAsksByUserIdAndAndAskStatus(userId, askStatus);
            if (pages % pageSize != 0) {
                pages = pages / pageSize + 1;
            } else {
                pages /= pageSize;
            }
            if (page == pages) {
                page = 0;
            }

            return askRepository.findUserAskListByWriterId(
                    userId,
                    askStatus,
                    PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id")));
        }
    }

    private static AskStatus getAskStatus(int type) {
        return type == 0 ? AskStatus.WAITING : type == 1 ? AskStatus.ACCEPTED : AskStatus.PARTICIPATION;
    }

    private static PostStatus getPostStatus(int type) {
        return type == 0 ? PostStatus.ONGOING : type == 1 ? PostStatus.COMPLETION : PostStatus.CLOSED;
    }
}
