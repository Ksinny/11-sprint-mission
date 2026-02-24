package com.sprint.mission.discodeit.entity;

import java.util.List;
import java.util.UUID;

public class Channel extends BaseEntity {
    private ChannelType type;
    private String name;
    private List<UUID> memberIds;

    public Channel(ChannelType type, String name, List<UUID> memberIds) {
        super();
        this.type = type;
        this.name = name;
        this.memberIds = memberIds;
    }

    public ChannelType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public List<UUID> getMemberIds() {
        return memberIds;
    }

    public void update(ChannelType type, String name, List<UUID> memberIds) {
        this.type = type;
        this.name = name;
        this.memberIds = memberIds;
        super.timeUpdate();
    }

    @Override
    public String toString() {
        return "Channel [" +
                "UUID: " + getId() +
                "\n이름: " + getName() +
                ", 타입: " + getType().getName() +
                ", 참여 인원: " + (getMemberIds() != null ? getMemberIds().size() : 0) + "명" +
                ", 생성 시간: " + getCreatedAt() +
                ", 수정 시간: " + getUpdatedAt() +
                ']';
    }


}