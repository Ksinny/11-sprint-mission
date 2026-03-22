package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

public class ChannelDto {

    // PUBLIC 채널 생성
    public record CreatePublicRequest(
        String name,
        String description
    ) {
        // DTO -> Entity
        public Channel toEntity() {
            return Channel.builder()
                    .name(this.name)
                    .description(this.description)
                    .type(ChannelType.PUBLIC)
                    .build();
        }

    }

    // PRIVATE 채널 생성
    public record CreatePrivateRequest(
        List<UUID> memberIds // 참여 유저 ID 목록 (name, description은 생략)
    ) {
        // DTO -> Entity
        public Channel toEntity() {
            return Channel.builder()
                    .type(ChannelType.PRIVATE)
                    .build();
        }
    }

    @Builder
    public record Response(
        UUID id,
        String name,
        String description,
        ChannelType type
    ) {
        // Entity -> DTO
        public static Response of(Channel channel) {
            return Response.builder()
                    .id(channel.getId())
                    .name(channel.getName())
                    .description(channel.getDescription())
                    .type(channel.getType())
                    .build();
        }
    }
}
