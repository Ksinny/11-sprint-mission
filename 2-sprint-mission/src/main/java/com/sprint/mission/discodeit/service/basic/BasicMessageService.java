package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Collection;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChannelService channelService;

    // 의존성 주입
    public BasicMessageService(MessageRepository messageRepository, UserService userService, ChannelService channelService) {
        this.messageRepository = messageRepository;
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
        if (channel == null) {
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

        // 메시지 중복 검증
        if (messageRepository.findById(message.getId()) != null) {
            System.out.println("이미 존재하는 메시지입니다. 메시지 전송 실패");
            return;
        }

        // 메시지 저장
        messageRepository.save(message);
        System.out.println("메시지가 전송 되었습니다.");
    }

    @Override
    public Message findById(UUID id) {
        return messageRepository.findById(id);
    }

    @Override
    public Collection<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public void update(UUID id, String content) {
        Message message = messageRepository.findById(id);
        if (message != null) {
            message.update(content);
            messageRepository.save(message); // 수정된 메시지 덮어쓰며 저장
            System.out.println("메시지가 수정되었습니다.");
        } else {
            System.out.println("해당 메시지를 찾을 수 없습니다.");
        }
    }

    @Override
    public void delete(UUID id) {
        Message message = messageRepository.findById(id);
        if (message != null) {
            messageRepository.delete(id); // 메시지 삭제
            System.out.println("메시지가 정상적으로 삭제되었습니다.");
        } else {
            System.out.println("해당 메시지를 찾을 수 없습니다.");
        }
    }
}