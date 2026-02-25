package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data;

    public JCFChannelService() {
        this.data = new HashMap<>();
    }

    @Override
    public void create(Channel channel) {
        // 중복 생성 방지
        if (data.containsKey(channel.getId())) {
            System.out.println("이미 존재하는 채널 ID입니다.");
            return;
        }
        data.put(channel.getId(), channel);
        System.out.println(channel.getName() + " 채널이 생성되었습니다.");
    }

    @Override
    public Channel findById(UUID id) {
        return data.get(id);
    }

    @Override
    public Collection<Channel> findAll() {
        return data.values();
    }

    @Override
    public void update(UUID id, ChannelType type, String name, List<UUID> memberIds) {
        Channel channel = data.get(id);
        if (channel != null) {
            channel.update(type, name, memberIds);
            System.out.println(name + " 채널 정보가 수정되었습니다.");
        } else {
            System.out.println("해당 채널을 찾을 수 없습니다.");
        }
    }

    @Override
    public void delete(UUID id) {
        Channel removedChannel = data.remove(id);
        if (removedChannel != null) {
            System.out.println("채널이 정상적으로 삭제되었습니다.");
        } else {
            System.out.println("해당 채널을 찾을 수 없습니다.");
        }
    }
}