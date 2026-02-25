package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import java.util.Collection;
import java.util.UUID;

public interface MessageService {
    // 생성
    void create(Message message);

    // 단건 조회
    Message findById(UUID id);

    // 전체 조회
    Collection<Message> findAll();

    // 수정
    void update(UUID id, String content);

    // 삭제
    void delete(UUID id);
}