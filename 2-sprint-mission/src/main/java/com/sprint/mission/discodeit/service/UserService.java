package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import java.util.Collection;
import java.util.UUID;

public interface UserService {
    // 생성
    void create(User user);

    // 단건 조회
    User findById(UUID id);

    // 전체 조회
    Collection<User> findAll();

    // 수정
    void update(UUID id, String userName, String nickname, String description, String email, String profileImage);

    // 삭제
    void delete(UUID id);
}