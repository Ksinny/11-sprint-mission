package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserMapper userMapper;

  public ChannelDto.Response toDto(Channel entity) {
    if (entity == null) {
      return null;
    }

    Instant lastMessageAt = messageRepository.findTopByChannelIdOrderByCreatedAtDesc(entity.getId())
        .map(Message::getCreatedAt)
        .orElse(null);

    List<UserDto.Response> participants = null;
    if (entity.getType() == ChannelType.PRIVATE) {
      participants = readStatusRepository.findAllByChannelId(entity.getId()).stream()
          .map(ReadStatus::getUser)
          .map(userMapper::toDto)
          .toList();
    }

    return ChannelDto.Response.builder()
        .id(entity.getId())
        .type(entity.getType())
        .name(entity.getName())
        .description(entity.getDescription())
        .participants(participants)
        .lastMessageAt(lastMessageAt)
        .build();
  }
}