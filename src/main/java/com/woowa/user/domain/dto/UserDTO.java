package com.woowa.user.domain.dto;

public record UserDTO(Long userId, String role, String name, String externalId, String socialCode) {
}
