package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.User;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

public class UserDto {

    @Builder
    public record CreateRequest(
            String userName,
            String password,
            String email,
            String nickname,
            String description,
            UUID profileImageId // 선택적으로 프로필 이미지 등록
    ) {
        public User toEntity() {
            return User.builder()
                    .userName(this.userName)
                    .password(this.password)
                    .email(this.email)
                    .nickname(this.nickname)
                    .description(this.description)
                    .profileImageId(this.profileImageId)
                    .build();
        }
    }

    @Builder
    public record Response(
            UUID id,
            String username,
            String email,
            boolean isOnline
    ) {}
}