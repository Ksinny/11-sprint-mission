package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class})
public abstract class UserMapper {

  // TODO: JwtRegistry 기반 교체 예정
  @Mapping(target = "online", constant = "false")
  public abstract UserDto.Response toDto(User user);
}