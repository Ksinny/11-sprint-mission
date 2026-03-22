package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;


    @Override
    public MessageDto.Response create(MessageDto.CreateRequest request) {
        if (!channelRepository.existsById(request.channelId())) {
            throw new NoSuchElementException("Channel not found with id " + request.channelId());
        }
        if (!userRepository.existsById(request.authorId())) {
            throw new NoSuchElementException("Author not found with id " + request.authorId());
        }

        Message message = request.toEntity();
        messageRepository.save(message);

        return MessageDto.Response.of(message);
    }


    @Override
    public List<MessageDto.Response> findAllByChannelId(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel not found with id " + channelId);
        }

        return messageRepository.findAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .map(MessageDto.Response::of)
                .toList();
    }

    @Override
    public Message update(UUID id, String content) {
//        Message message = findById(id);
//        message.update(content);
//        return messageRepository.save(message);
        return null;
    }

    @Override
    public void delete(UUID id) {
        if (!messageRepository.existsById(id)) {
            throw new NoSuchElementException("Message with id " + id + " not found");
        }
        messageRepository.deleteById(id);
    }
}