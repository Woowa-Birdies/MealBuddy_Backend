package com.woowa.gather.repository;

import com.woowa.gather.domain.Ask;
import com.woowa.gather.domain.dto.AskListResponse;
import com.woowa.gather.domain.dto.UserPostListResponse;
import com.woowa.gather.domain.enums.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AskRepository extends JpaRepository<Ask, Long> {

    @Query("select new com.woowa.gather.domain.dto.AskListResponse(" +
            "a.id, p.id, u.id, p.foodTypeTag, p.genderTag, p.ageTag, l.address, l.place, p.participantTotal, " +
            "p.participantCount, p.postStatus, a.askStatus, p.meetAt, p.closeAt, p.createdAt) " +
            "from Ask a " +
            "join Post p on a.post = p " +
            "join Location l on a.post.location = l " +
            "join User u on a.user = u " +
            "where u.id = :userId and p.postStatus = :postStatus")
    Optional<List<AskListResponse>> findAskListByWriterId(@Param("userId") Long userId, @Param("postStatus") PostStatus postStatus);
}
