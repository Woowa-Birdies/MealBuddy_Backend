package com.woowa.gather.repository;

import com.woowa.gather.domain.Ask;
import com.woowa.gather.domain.Post;
import com.woowa.gather.domain.dto.AskListResponse;
import com.woowa.gather.domain.dto.PostAskListResponse;
import com.woowa.gather.domain.enums.AskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AskRepository extends JpaRepository<Ask, Long> {

    /** 유저가 신청한 리스트 */
    @Query("select new com.woowa.gather.domain.dto.AskListResponse(" +
            "a.id, p.id, u.id, p.foodTypeTag, p.genderTag, p.ageTag, l.address, l.place, p.participantTotal, " +
            "p.participantCount, p.postStatus, a.askStatus, p.meetAt, p.closeAt, p.createdAt) " +
            "from Ask a " +
            "join Post p on a.post = p " +
            "join Location l on a.post.location = l " +
            "join User u on a.user = u " +
            "where u.id = :userId and a.askStatus = :askStatus " +
            "order by a.id desc " +
            "limit 3")
    Optional<List<AskListResponse>> findUserAskListByWriterId(
            @Param("userId") Long userId,
            @Param("askStatus") AskStatus askStatus);

    @Query("select new com.woowa.gather.domain.dto.AskListResponse(" +
            "a.id, p.id, u.id, p.foodTypeTag, p.genderTag, p.ageTag, l.address, l.place, p.participantTotal, " +
            "p.participantCount, p.postStatus, a.askStatus, p.meetAt, p.closeAt, p.createdAt) " +
            "from Ask a " +
            "join Post p on a.post = p " +
            "join Location l on a.post.location = l " +
            "join User u on a.user = u " +
            "where u.id = :userId and a.askStatus = :askStatus " +
            "and a.id < :askId " +
            "order by a.id desc " +
            "limit 3")
    Optional<List<AskListResponse>> findUserAskListByWriterId(
            @Param("userId") Long userId,
            @Param("askStatus") AskStatus askStatus,
            @Param("askId") Long askId);

    @Query("select new com.woowa.gather.domain.dto.AskListResponse(" +
            "a.id, p.id, u.id, p.foodTypeTag, p.genderTag, p.ageTag, l.address, l.place, p.participantTotal, " +
            "p.participantCount, p.postStatus, a.askStatus, p.meetAt, p.closeAt, p.createdAt) " +
            "from Ask a " +
            "join Post p on a.post = p " +
            "join Location l on a.post.location = l " +
            "join User u on a.user = u " +
            "where u.id = :userId and a.askStatus = 'WAITING' or a.askStatus = 'REJECTED' " +
            "order by a.id desc " +
            "limit 3")
    Optional<List<AskListResponse>> findWaitingOrRejectedAskList(
            @Param("userId") Long userId);

    @Query("select new com.woowa.gather.domain.dto.AskListResponse(" +
            "a.id, p.id, u.id, p.foodTypeTag, p.genderTag, p.ageTag, l.address, l.place, p.participantTotal, " +
            "p.participantCount, p.postStatus, a.askStatus, p.meetAt, p.closeAt, p.createdAt) " +
            "from Ask a " +
            "join Post p on a.post = p " +
            "join Location l on a.post.location = l " +
            "join User u on a.user = u " +
            "where u.id = :userId and a.askStatus in (com.woowa.gather.domain.enums.AskStatus.WAITING, com.woowa.gather.domain.enums.AskStatus.REJECTED) "+
            "and a.id < :askId " +
            "order by a.id desc " +
            "limit 3")
    Optional<List<AskListResponse>> findWaitingOrRejectedAskList(
            @Param("userId") Long userId,
            @Param("askId") Long askId);

    /** 모집글 신청자 리스트 */
    @Query("select new com.woowa.gather.domain.dto.PostAskListResponse(" +
            "u.id, a.askStatus, u.gender, u.birthDate, u.introduce, u.image) " +
            "from Ask a " +
            "join Post p on a.post = p " +
            "join User u on a.user = u " +
            "where p.id = :postId and a.askStatus = 'WAITING'")
    Optional<List<PostAskListResponse>> findAskedUserByPostId(@Param("postId") Long postId);

    @Query("select new com.woowa.gather.domain.dto.PostAskListResponse(" +
            "u.id, a.askStatus, u.gender, u.birthDate, u.introduce, u.image) " +
            "from Ask a " +
            "join Post p on a.post = p " +
            "join User u on a.user = u " +
            "where p.id = :postId and a.askStatus = 'ACCEPTED'")
    Optional<List<PostAskListResponse>> findAcceptedUserByPostId(@Param("postId") Long postId);

    @Query("select new com.woowa.gather.domain.dto.PostAskListResponse(" +
            "u.id, a.askStatus, u.gender, u.birthDate, u.introduce, u.image) " +
            "from Ask a " +
            "join Post p on a.post = p " +
            "join User u on a.user = u " +
            "where p.id = :postId and a.askStatus = 'PARTICIPATION'")
    Optional<List<PostAskListResponse>> findParticipatedUserByPostId(@Param("postId") Long postId);

    @Query("select count(a.id) from Ask a where a.askStatus = 'PARTICIPATION' and a.post = :post")
    int countParticipantCountByPostId(@Param("post") Post post);

}
