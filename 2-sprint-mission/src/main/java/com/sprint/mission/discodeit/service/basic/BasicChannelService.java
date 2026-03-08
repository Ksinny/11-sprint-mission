package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    // 의존성 주입
    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public void create(Channel channel) {
        // 채널 중복 검증
        if (channelRepository.findById(channel.getId()) != null) {
            System.out.println("이미 존재하는 채널 ID입니다.");
            return;
        }
        
        // 채널 저장
        channelRepository.save(channel);
        System.out.println(channel.getName() + " 채널이 생성되었습니다.");
    }

    @Override
    public Channel findById(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public Collection<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public void update(UUID id, ChannelType type, String name, List<UUID> memberIds) {
        Channel channel = channelRepository.findById(id);
        if (channel != null) {
            channel.update(type, name, memberIds);

            channelRepository.save(channel); // 수정된 채널 덮어쓰며 저장
            System.out.println(name + " 채널 정보가 수정되었습니다.");
        } else {
            System.out.println("해당 채널을 찾을 수 없습니다.");
        }
    }

    @Override
    public void delete(UUID id) {
        Channel channel = channelRepository.findById(id);
        if (channel != null) {
            channelRepository.delete(id); // 채널 삭제
            System.out.println("채널이 정상적으로 삭제되었습니다.");
        } else {
            System.out.println("해당 채널을 찾을 수 없습니다.");
        }
    }
}