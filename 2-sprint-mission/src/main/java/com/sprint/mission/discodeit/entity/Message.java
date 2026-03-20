package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity {
    private String content;
    private final UUID authorId;
    private final UUID channelId;

    public Message(String content, UUID authorId, UUID channelId) {
        super();
        this.content = content;
        this.authorId = authorId;
        this.channelId = channelId;
    }

    public String getContent() {
        return content;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public UUID getChannelId() {
        return channelId;
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