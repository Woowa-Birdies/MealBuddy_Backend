package com.woowa.review;

import com.woowa.chat.domain.UserStatus;
import com.woowa.chat.repository.UserStatusRepository;
import com.woowa.review.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private UserStatusRepository userStatusRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    public void testGetUserIds() {
        // Mock 데이터 생성
        Long roomId = 1L;
        UserStatus user1 = UserStatus.builder().userId(1L).roomId(roomId).build();
        UserStatus user2 = UserStatus.builder().userId(2L).roomId(roomId).build();
        List<UserStatus> userList = Arrays.asList(user1, user2);

        // Mock repository 동작 설정
        when(userStatusRepository.findByRoomId(anyLong())).thenReturn(userList);

        // 테스트 대상 메서드 호출
        List<Long> userIds = reviewService.getUserIds(roomId);

        // 결과 검증
        assertEquals(2, userIds.size());
        assertEquals(1L, userIds.get(0));
        assertEquals(2L, userIds.get(1));
    }
}

