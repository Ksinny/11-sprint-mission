package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.PageResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface MessageService {

  MessageDto.Response create(MessageDto.CreateRequest request,
      List<BinaryContentDto.CreateRequest> fileRequests);

  PageResponse<MessageDto.Response> findAllByChannelId(UUID channelId, Pageable pageable);

  MessageDto.Response update(UUID id, MessageDto.UpdateRequest request);

  void delete(UUID id);
}