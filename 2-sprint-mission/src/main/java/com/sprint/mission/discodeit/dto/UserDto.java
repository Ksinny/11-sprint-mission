package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

public class UserDto {

    @Builder
    public record CreateRequest(
            String username,
            String nickname,
            String description,
            String email,
            String password
    ) {
        // DTO -> Entity
        public User toEntity(UUID profileImageId) {
            return User.builder()
                    .username(this.username)
                    .nickname(this.nickname)
                    .description(this.description)
                    .email(this.email)
                    .password(this.password)
                    .profileImageId(profileImageId)
                    .build();
        }
    }

    public record UpdateRequest(
            String username,
            String nickname,
            String description,
            String email,
            String password,
            UUID profileImageId
    ) {}

    @Builder
    public record Response(
            UUID id,
            String username,
            String nickname,
            String description,
            String email,
            UUID profileImageId,
            boolean isOnline,
            Instant lastActivityAt // 추가
    ) {

        // Entity -> DTO
        public static Response of(User user, UserStatus status) {
            return Response.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .nickname(user.getNickname())
                    .description(user.getDescription())
                    .email(user.getEmail())
                    .profileImageId(user.getProfileImageId())
                    .isOnline(status.isOnline())
                    .lastActivityAt(status.getLastActiveAt())
                    .build();
        }
    }
}