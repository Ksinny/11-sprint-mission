package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageDto.Response create(MessageDto.CreateRequest request);
    Message findById(UUID id);
    List<Message> findAll();
    Message update(UUID id, String content);
    void delete(UUID id);
}