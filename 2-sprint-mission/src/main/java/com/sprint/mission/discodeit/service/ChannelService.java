package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelDto.Response  createPublicChannel(ChannelDto.CreatePublicRequest request);
    ChannelDto.Response createPrivateChannel(ChannelDto.CreatePrivateRequest request);
    ChannelDto.Response findById(UUID id);
    List<ChannelDto.Response> findAllByUserId(UUID userId);
    Channel update(UUID id, ChannelType type, String name, String description, List<UUID> memberIds);
    void delete(UUID id);
}