package com.woowa.chat.domain.dto;

import java.time.LocalDateTime;

public record ChatRequestDto(Long roomId, LocalDateTime entryAt, int page, int offset) {
}
