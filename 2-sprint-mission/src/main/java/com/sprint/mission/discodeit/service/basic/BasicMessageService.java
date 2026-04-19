package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.MessageDto.CreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.BusinessException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;

  @Override
  public MessageDto.Response create(CreateRequest request,
      List<BinaryContentDto.CreateRequest> fileRequests) {
    Channel channel = channelRepository.findById(request.channelId())
        .orElseThrow(() -> new BusinessException(ErrorCode.CHANNEL_NOT_FOUND));

    User author = userRepository.findById(request.authorId())
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    List<BinaryContent> attachments = (fileRequests != null)
        ? fileRequests.stream().map(BinaryContentDto.CreateRequest::toEntity).toList()
        : new ArrayList<>();

    Message message = request.toEntity(channel, author, attachments);
    messageRepository.save(message);

    return MessageDto.Response.of(message);
  }


  @Override
  public List<MessageDto.Response> findAllByChannelId(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw new BusinessException(ErrorCode.CHANNEL_NOT_FOUND);
    }

    return messageRepository.findAllByChannelId(channelId).stream()
        .map(MessageDto.Response::of)
        .toList();
  }

  @Override
  @Transactional
  public MessageDto.Response update(UUID id, MessageDto.UpdateRequest request) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ErrorCode.MESSAGE_NOT_FOUND));
    message.update(request.newContent());

    return MessageDto.Response.of(message);
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ErrorCode.MESSAGE_NOT_FOUND));

    messageRepository.deleteById(id);
  }
}