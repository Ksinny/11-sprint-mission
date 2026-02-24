package com.sprint.mission.discodeit.entity;

public enum ChannelType {
    PUBLIC("공개 채널"),
    PRIVATE("비공개 채널"),
    DM("개인 메시지");

    private final String name;

    ChannelType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
