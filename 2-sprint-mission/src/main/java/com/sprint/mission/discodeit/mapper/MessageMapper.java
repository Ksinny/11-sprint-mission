package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {

  private final UserMapper userMapper;
  private final BinaryContentMapper binaryContentMapper;

  public MessageDto.Response toDto(Message entity) {
      if (entity == null) {
          return null;
      }

    return MessageDto.Response.builder()
        .id(entity.getId())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .content(entity.getContent())
        .channelId(entity.getChannel().getId())
        .author(userMapper.toDto(entity.getAuthor()))
        .attachments(entity.getAttachments().stream()
            .map(binaryContentMapper::toDto)
            .toList())
        .build();
  }
}