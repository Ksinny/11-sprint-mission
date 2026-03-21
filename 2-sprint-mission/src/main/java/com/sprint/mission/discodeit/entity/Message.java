package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BaseEntity {
    private String content;
    private final UUID authorId;
    private final UUID channelId;
    private List<UUID> attachmentIds = new ArrayList<>();

    public Message(String content, UUID authorId, UUID channelId, List<UUID> attachmentIds) {
        super();
        this.content = content;
        this.authorId = authorId;
        this.channelId = channelId;

        if (attachmentIds != null) {
            this.attachmentIds.addAll(attachmentIds);
        }
    }

    // 메시지 내용만 수정 가능
    public void update(String newContent) {
        boolean anyValueUpdated = false;

        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            super.timeUpdate();
        }
    }

    // 첨부파일 추가
    public void addAttachment(UUID attachmentId) {
        if (this.attachmentIds == null) {
            this.attachmentIds = new ArrayList<>();
        }
        this.attachmentIds.add(attachmentId);
        super.timeUpdate();
    }

    @Override
    public String toString() {
        return "Message [" +
                "UUID: " + getId() +
                "\n발신자 ID: " + getAuthorId() +
                ", 채널 ID: " + getChannelId() +
                ", 내용: " + getContent() +
                ", 작성 시간: " + getCreatedAt() +
                ", 수정 시간: " + getUpdatedAt() +
                "]\n";
    }
}