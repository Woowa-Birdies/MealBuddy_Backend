package com.woowa.common.config;

import com.woowa.gather.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulerConfig {
    private final PostRepository postRepository;

    @Scheduled(cron = "0 0/1 * 1/1 * *", zone = "Asia/Seoul")
    @Transactional
    public void updatePostStatus() {
        int i = postRepository.updatePosts();
    }
}
