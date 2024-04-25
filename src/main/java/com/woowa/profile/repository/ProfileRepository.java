package com.woowa.profile.repository;

import com.woowa.gather.domain.Ask;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProfileRepository extends JpaRepository<Ask,Long> {
    @Query("SELECT a.post.id FROM Ask a WHERE a.user.id = :userId AND a.askStatus = 'PARTICIPATION' ORDER BY a.id DESC")
    List<Long> findMyParticipationInfoByUserId(@Param("userId") Long userId, Pageable pageable);
}

