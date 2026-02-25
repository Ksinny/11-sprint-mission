package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    // 생성
    void create(Channel channel);

    // 단건 조회
    Channel findById(UUID id);

    // 전체 조회
    Collection<Channel> findAll();

    // 수정
    void update(UUID id, ChannelType type, String name, List<UUID> memberIds);

    // 삭제
    void delete(UUID id);
}