package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;

    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.data = new HashMap<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public void create(Message message) {
        // 유저 검증
        if (userService.findById(message.getSenderId()) == null) {
            System.out.println("존재하지 않는 유저입니다. 메시지 전송 실패");
            return;
        }

        // 채널 검증
        Channel channel = channelService.findById(message.getChannelId());
        if (channelService.findById(message.getChannelId()) == null) {
            System.out.println("존재하지 않는 채널입니다. 메시지 전송 실패");
            return;
        }

        // 채널 멤버 검증
        if (channel.getType() == ChannelType.PRIVATE || channel.getType() == ChannelType.DM) {
            if (!channel.getMemberIds().contains(message.getSenderId())) {
                System.out.println("해당 채널의 멤버가 아닙니다. 메시지 전송 실패");
                return;
            }
        }

        // 중복 생성 방지
        if (data.containsKey(message.getId())) {
            System.out.println("이미 존재하는 메시지입니다. 메시지 전송 실패");
            return;
        }
        data.put(message.getId(), message);
        System.out.println("메시지가 전송 되었습니다.");
    }

    @Override
    public Message findById(UUID id) {
        return data.get(id);
    }

    @Override
    public Collection<Message> findAll() {
        return data.values();
    }

    @Override
    public void update(UUID id, String content) {
        Message message = data.get(id);
        if (message != null) {
            message.update(content);
            System.out.println(content + "로 메시지가 수정되었습니다.");
        } else {
            System.out.println("해당 메시지를 찾을 수 없습니다.");
        }
    }

    @Override
    public void delete(UUID id) {
        Message removedMessage = data.remove(id);
        if (removedMessage != null) {
            System.out.println("메시지가 정상적으로 삭제되었습니다.");
        } else {
            System.out.println("해당 메시지를 찾을 수 없습니다.");
        }
    }
}