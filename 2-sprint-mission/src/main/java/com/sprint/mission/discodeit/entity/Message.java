package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity {
    private String content;
    private final UUID senderId;
    private final UUID channelId;

    public Message(String content, UUID senderId, UUID channelId) {
        super();
        this.content = content;
        this.senderId = senderId;
        this.channelId = channelId;
    }

    public String getContent() {
        return content;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    // 메시지 내용만 수정 가능
    public void update(String content) {
        this.content = content;
        super.timeUpdate();
    }

    @Override
    public String toString() {
        return "Message [" +
                "UUID: " + getId() +
                "\n발신자 ID: " + getSenderId() +
                ", 채널 ID: " + getChannelId() +
                ", 내용: " + getContent() +
                ", 작성 시간: " + getCreatedAt() +
                ", 수정 시간: " + getUpdatedAt() +
                "]\n";
    }
}