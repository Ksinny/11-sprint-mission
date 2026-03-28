package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageDto {

    public record CreateRequest(
            UUID authorId,
            UUID channelId,
            String content
    ) {
        // DTO -> Entity
        public Message toEntity(List<UUID> attachmentIds) {
            return Message.builder()
                    .authorId(this.authorId)
                    .channelId(this.channelId)
                    .content(this.content)
                    .attachmentIds(attachmentIds != null ? new ArrayList<>(attachmentIds) : new ArrayList<>())
                    .build();
        }
    }

    public record UpdateRequest(
            String content
    ) {}

    public record Response(
        UUID id,
        String content,
        List<UUID> attachmentIds,
        Instant createdAt
    ) {
        // Entity -> DTO
        public static Response of(Message message) {
            return new Response(
                message.getId(),
                message.getContent(),
                message.getAttachmentIds(),
                message.getCreatedAt()
            );
        }
    }
}