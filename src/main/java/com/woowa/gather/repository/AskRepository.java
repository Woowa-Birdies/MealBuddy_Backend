package com.woowa.gather.repository;

import com.woowa.gather.domain.Ask;
import com.woowa.gather.domain.Post;
import com.woowa.gather.domain.dto.AskListResponse;
import com.woowa.gather.domain.dto.PostAskListResponse;
import com.woowa.gather.domain.enums.AskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AskRepository extends JpaRepository<Ask, Long> {
    @Query("select count(a.id) from Ask a where a.user.id = :id and a.askStatus = 'WAITING' or a.askStatus = 'REJECTED'")
    int countAsksByUserIdAndAndAskStatus(Long id);
    int countAsksByUserIdAndAndAskStatus(Long id, AskStatus askStatus);

    int countAsksByPostIdAndAskStatus(Long id, AskStatus askStatus);

    @Query("select a from Ask a where a.post.id = :postId and a.user.id = :userId")
    Optional<Ask> findByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);

    /** 유저가 신청한 리스트 */
    @Query("select new com.woowa.gather.domain.dto.AskListResponse(" +
            "a.id, p.id, u.id, p.foodTypeTag, p.genderTag, p.ageTag, l.address, l.place, p.participantTotal, " +
            "p.participantCount, p.postStatus, a.askStatus, p.meetAt, p.closeAt, p.createdAt) " +
            "from Ask a " +
            "join Post p on a.post = p " +
            "join Location l on a.post.location = l " +
            "join User u on a.user = u " +
            "where u.id = :userId and a.askStatus = 'WAITING' or a.askStatus = 'REJECTED' ")
    Page<AskListResponse> findWaitingOrRejectedAskList(
            @Param("userId") Long userId,
            PageRequest pageRequest);

    @Query("select new com.woowa.gather.domain.dto.AskListResponse(" +
            "a.id, p.id, u.id, p.foodTypeTag, p.genderTag, p.ageTag, l.address, l.place, p.participantTotal, " +
            "p.participantCount, p.postStatus, a.askStatus, p.meetAt, p.closeAt, p.createdAt) " +
            "from Ask a " +
            "join Post p on a.post = p " +
            "join Location l on a.post.location = l " +
            "join User u on a.user = u " +
            "where u.id = :userId and a.askStatus = :askStatus ")
    Page<AskListResponse> findUserAskListByWriterId(
            @Param("userId") Long userId,
            @Param("askStatus") AskStatus askStatus, PageRequest pageRequest);

    /** 모집글 신청자 리스트 */
    @Query("select new com.woowa.gather.domain.dto.PostAskListResponse(" +
            "u.id, a.id, a.askStatus, u.gender, u.birthDate, u.introduce, u.image) " +
            "from Ask a " +
            "join Post p on a.post = p " +
            "join User u on a.user = u " +
            "where p.id = :postId and a.askStatus = :askStatus ")
    Page<PostAskListResponse> findAskedUserByPostId(
            @Param("postId") Long postId,
            @Param("askStatus") AskStatus askStatus,
            PageRequest pageRequest);

    @Query("select count(a.id) from Ask a where a.askStatus = 'PARTICIPATION' and a.post = :post")
    int countParticipantCountByPostId(@Param("post") Post post);

}
