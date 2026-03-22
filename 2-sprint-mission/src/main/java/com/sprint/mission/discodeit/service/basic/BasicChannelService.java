package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public ChannelDto.Response createPublicChannel(ChannelDto.CreatePublicRequest request) {
        Channel channel = request.toEntity();
        channelRepository.save(channel);
        return ChannelDto.Response.of(channel);
    }

    @Override
    public ChannelDto.Response createPrivateChannel(ChannelDto.CreatePrivateRequest request) {
        Channel channel = request.toEntity();
        channelRepository.save(channel);

        // 채널 참여자 ReadStatus 생성
        if (request.memberIds() != null) {
            request.memberIds().forEach(userId -> {
                ReadStatus readStatus = ReadStatus.builder()
                        .userId(userId)
                        .channelId(channel.getId())
                        .build();
                readStatusRepository.save(readStatus);
            });
        }

        return ChannelDto.Response.of(channel);
    }

    @Override
    public Channel findById(UUID id) {
        return channelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + id + " not found"));
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID id, ChannelType type, String name, String description, List<UUID> memberIds) {
        Channel channel = findById(id);
        channel.update(type, name, description, memberIds);
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID id) {
        if (!channelRepository.existsById(id)) {
            throw new NoSuchElementException("Channel with id " + id + " not found");
        }
        channelRepository.deleteById(id);
    }
}