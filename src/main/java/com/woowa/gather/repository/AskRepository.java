package com.woowa.gather.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.woowa.gather.domain.Ask;
import com.woowa.gather.domain.Post;
import com.woowa.gather.domain.dto.AskListResponse;
import com.woowa.gather.domain.dto.PostAskListResponse;
import com.woowa.gather.domain.enums.AskStatus;
import com.woowa.user.domain.User;

public interface AskRepository extends JpaRepository<Ask, Long> {

	@Query("select new com.woowa.gather.domain.dto.AskListResponse(" +
		"a.id, p.id, u.id, p.foodTypeTag, p.genderTag, p.ageTag, l.address, l.place, p.participantTotal, " +
		"p.participantCount, p.postStatus, a.askStatus, p.meetAt, p.closeAt, p.createdAt) " +
		"from Ask a " +
		"join Post p on a.post = p " +
		"join Location l on a.post.location = l " +
		"join User u on a.user = u " +
		"where u.id = :userId and a.askStatus = :askStatus")
	Optional<List<AskListResponse>> findUserAskListByWriterId(@Param("userId") Long userId,
		@Param("askStatus") AskStatus askStatus);

	@Query("select new com.woowa.gather.domain.dto.AskListResponse(" +
		"a.id, p.id, u.id, p.foodTypeTag, p.genderTag, p.ageTag, l.address, l.place, p.participantTotal, " +
		"p.participantCount, p.postStatus, a.askStatus, p.meetAt, p.closeAt, p.createdAt) " +
		"from Ask a " +
		"join Post p on a.post = p " +
		"join Location l on a.post.location = l " +
		"join User u on a.user = u " +
		"where u.id = :userId and a.askStatus = 'WAITING' or a.askStatus = 'REJECTED'")
	Optional<List<AskListResponse>> findWaitingOrRejectedAskList(@Param("userId") Long userId);

	@Query("select new com.woowa.gather.domain.dto.PostAskListResponse(" +
		"u.id, a.askStatus, u.gender, u.birthDate, u.introduce)" +
		"from Ask a " +
		"join Post p on a.post = p " +
		"join User u on a.user = u " +
		"where p.id = :postId")
	Optional<List<PostAskListResponse>> findAskedUserByPostId(@Param("postId") Long postId);

	@Query("select count(a.id) from Ask a where a.askStatus = :askStatus and a.post = :post")
	int countParticipantCountByPostId(@Param("askStatus") AskStatus askStatus, @Param("post") Post post);

	boolean existsAskByUserAndPost(User user, Post post);

}
