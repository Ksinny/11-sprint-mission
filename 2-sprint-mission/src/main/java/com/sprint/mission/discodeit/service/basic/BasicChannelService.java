package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    // 의존성 주입
    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(ChannelType type, String name, String description, List<UUID> memberIds) {
        Channel channel = new Channel(type, name, description, memberIds);

        // 채널 저장
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        Channel channel = channelRepository.findById(id);
        if (channel == null) {
            throw new NoSuchElementException("Channel with id " + id + " not found");
        }
        return channel;
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channelRepository.findAll());
    }

    @Override
    public Channel update(UUID id, ChannelType type, String name, String description, List<UUID> memberIds) {
        Channel channel = findById(id);
        channel.update(type, name, description, memberIds);

        channelRepository.save(channel); // 수정된 채널 덮어쓰며 저장
        return channel;
    }

    @Override
    public void delete(UUID id) {
        findById(id);

        channelRepository.delete(id); // 채널 삭제
    }
}