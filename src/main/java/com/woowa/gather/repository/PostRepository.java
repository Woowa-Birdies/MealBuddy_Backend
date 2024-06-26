package com.woowa.gather.repository;

import com.woowa.gather.domain.Post;
import com.woowa.gather.domain.dto.PostDetailsResponseDto;
import com.woowa.gather.domain.dto.PostListResponse;
import com.woowa.gather.domain.enums.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    int countPostsByUserIdAndPostStatus(Long id, PostStatus postStatus);

    @Query("select count(p.id) from Post p where p.id = :postId and p.user.id = :userId")
    int findByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);

    @Query("select new com.woowa.gather.domain.dto.PostListResponse(" +
            "p.id, u.id, p.foodTypeTag, p.genderTag, p.ageTag, l.address, l.place, p.participantTotal, " +
            "p.participantCount, p.postStatus, p.meetAt, p.closeAt, p.createdAt) " +
            "from Post p " +
            "join Location l on p.location = l " +
            "left join User u on p.user = u " +
            "where u.id = :userId and p.postStatus = :postStatus ")
    Page<PostListResponse> findPostListByWriterId(
            @Param("userId") Long userId,
            @Param("postStatus") PostStatus postStatus,
            PageRequest pageRequest);

//    @Query("select new com.woowa.gather.domain.dto.PostDetailsResponseDto(" +
//            "p.id, u.id, u.nickname, p.meetAt, p.closeAt, p.foodTypeTag, p.ageTag, p.genderTag, " +
//            "p.participantTotal, p.participantCount, p.postStatus, l.place, l.address, p.contents) " +
//            "from Post p " +
//            "join Location l on p.location = l " +
//            "left join User u on p.user = u " +
//            "where p.id = :postId")
//    Optional<PostDetailsResponseDto> findPostDetailsByPostId(@Param("postId") Long postId);

    @Query("select new com.woowa.gather.domain.dto.PostDetailsResponseDto(" +
            "p.id, u.id, u.nickname, p.meetAt, p.closeAt, p.foodTypeTag, p.ageTag, p.genderTag, " +
            "p.participantTotal, p.participantCount, p.postStatus, l.place, l.address, p.contents, a.askStatus) " +
            "from Post p " +
            "join Location l on p.location = l " +
            "left join User u on p.user = u " +
            "left join p.asks a with a.user.id = :userId " +
            "where p.id = :postId")
    Optional<PostDetailsResponseDto> findPostDetailsByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);

    @Query("select new com.woowa.gather.domain.dto.PostListResponse(" +
            "p.id, u.id, p.foodTypeTag, p.genderTag, p.ageTag, l.address, l.place, " +
            "p.participantTotal, p.participantCount, p.postStatus, p.meetAt, p.closeAt, p.createdAt) " +
            "from Post p " +
            "join Location l on p.location = l " +
            "left join User u on p.user = u " +
            "where p.postStatus = 'ONGOING' " +
            "and p.closeAt >= current_date " +
            "and p.closeAt <= :targetDate " +
            "order by p.closeAt asc")
    List<PostListResponse> findDuePosts(@Param("targetDate") LocalDateTime targetDate);

    @Modifying(clearAutomatically = true)
    @Query("update Post p set p.postStatus = 'CLOSED' , p.updatedAt = :now " +
            "where p.closeAt <= :now " +
            "and p.postStatus " +
            "in (com.woowa.gather.domain.enums.PostStatus.ONGOING, com.woowa.gather.domain.enums.PostStatus.COMPLETION)")
    int updatePosts(LocalDateTime now);

    @Query("SELECT p FROM Post p WHERE p.user.id = :userId ORDER BY p.createdAt DESC")
    List<Post> findMyRecruitmentInfoByUserId(@Param("userId") Long userId, Pageable pageable);

}
