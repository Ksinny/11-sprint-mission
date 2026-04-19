package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Component;

@Component
public class BinaryContentMapper {

  public BinaryContentDto.Response toDto(BinaryContent entity) {
    if (entity == null) {
      return null;
    }

    return BinaryContentDto.Response.builder()
        .id(entity.getId())
        .fileName(entity.getFileName())
        .size(entity.getSize())
        .contentType(entity.getContentType())
        .build();
  }
}