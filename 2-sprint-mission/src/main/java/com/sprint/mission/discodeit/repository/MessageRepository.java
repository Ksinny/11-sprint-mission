package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  Optional<Message> findTopByChannelIdOrderByCreatedAtDesc(UUID id);

  void deleteByChannelId(UUID id);

  Slice<Message> findAllByChannelId(UUID channelId, Pageable pageable);
}
