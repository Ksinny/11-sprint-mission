package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
    public Message create(String content, UUID authorId, UUID channelId) {
        userService.findById(authorId); // 유저 검증
        Channel channel = channelService.findById(channelId); // 채널 검증

        // 채널 접근 권한 검증
        if (channel.getType() == ChannelType.PRIVATE || channel.getType() == ChannelType.DM) {
            if (channel.getMemberIds() == null || !channel.getMemberIds().contains(authorId)) {
                throw new IllegalArgumentException("User " + authorId + " is not a member of channel " + channelId);
            }
        }

        Message message = new Message(content, authorId, channelId);

        // 메시지 저장
        messageRepository.save(message);
        return message;
    }

    @Override
    public Message findById(UUID id) {
        Message message = messageRepository.findById(id);
        if (message == null) {
            throw new NoSuchElementException("Message with id " + id + " not found");
        }
        return message;
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messageRepository.findAll());
    }

    @Override
    public Message update(UUID id, String content) {
        Message message = findById(id);
        message.update(content);

        messageRepository.save(message); // 수정된 메시지 덮어쓰며 저장
        return message;
    }

    @Override
    public void delete(UUID id) {
        findById(id);

        messageRepository.delete(id); // 메시지 삭제
    }
}