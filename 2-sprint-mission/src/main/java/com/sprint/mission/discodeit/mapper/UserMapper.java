package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

  private final BinaryContentMapper binaryContentMapper;

  public UserDto.Response toDto(User entity) {
      if (entity == null) {
          return null;
      }

    boolean isOnline = entity.getStatus() != null && entity.getStatus().isOnline();

    return UserDto.Response.builder()
        .id(entity.getId())
        .username(entity.getUsername())
        .email(entity.getEmail())
        .profile(binaryContentMapper.toDto(entity.getProfile()))
        .online(isOnline)
        .build();
  }
}