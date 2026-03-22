package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto.Response create(UserDto.CreateRequest request);
    User findById(UUID id);
    List<User> findAll();
    User update(UUID id, String userName, String nickname, String description, String email, String password, UUID profileImageId);
    void delete(UUID id);
}