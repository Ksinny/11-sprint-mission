package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Component;

@Component
public class ReadStatusMapper {

  public ReadStatusDto.Response toDto(ReadStatus entity) {
      if (entity == null) {
          return null;
      }

    return ReadStatusDto.Response.builder()
        .id(entity.getId())
        .userId(entity.getUser().getId())
        .channelId(entity.getChannel().getId())
        .lastReadAt(entity.getLastReadAt())
        .build();
  }
}