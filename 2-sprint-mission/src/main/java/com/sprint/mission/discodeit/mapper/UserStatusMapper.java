package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserStatusMapper {

  public UserStatusDto.Response toDto(UserStatus entity) {
      if (entity == null) {
          return null;
      }

    return UserStatusDto.Response.builder()
        .id(entity.getId())
        .userId(entity.getUser().getId())
        .lastActiveAt(entity.getLastActiveAt())
        .build();
  }
}