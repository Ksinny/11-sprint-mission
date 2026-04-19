package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.exception.BusinessException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public ChannelDto.Response createPublicChannel(ChannelDto.CreatePublicRequest request) {
    Channel channel = request.toEntity();
    channelRepository.save(channel);

    return toResponse(channel);
  }

  @Override
  @Transactional
  public ChannelDto.Response createPrivateChannel(ChannelDto.CreatePrivateRequest request) {
    Channel channel = request.toEntity();
    channelRepository.save(channel);
    if (request.participantIds() != null && !request.participantIds().isEmpty()) {
      List<User> participants = userRepository.findAllById(request.participantIds());

      List<ReadStatus> readStatuses = participants.stream()
          .map(user -> ReadStatus.builder()
              .user(user)
              .channel(channel)
              .lastReadAt(channel.getCreatedAt())
              .build())
          .toList();

      readStatusRepository.saveAll(readStatuses);
    }
    return toResponse(channel);
  }

  // 공통 로직
  private ChannelDto.Response toResponse(Channel channel) {
    // 가장 최신 메시지
    Instant lastMessageAt = messageRepository.findTopByChannelIdOrderByCreatedAtDesc(
            channel.getId())
        .map(BaseEntity::getCreatedAt)
        .orElse(null);

    List<UUID> participantIds = null;
    if (channel.getType() == ChannelType.PRIVATE) {
      participantIds = readStatusRepository.findAllByChannelId(channel.getId()).stream()
          .map(readStatus -> readStatus.getChannel().getId())
          .toList();
    }
    return ChannelDto.Response.of(channel, lastMessageAt, participantIds);
  }

  @Override
  public ChannelDto.Response findById(UUID id) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ErrorCode.CHANNEL_NOT_FOUND));

    return toResponse(channel);
  }

  @Override
  public List<ChannelDto.Response> findAllByUserId(UUID userId) {
    List<UUID> myChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatus -> readStatus.getChannel().getId())
        .toList();

    return channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, myChannelIds)
        .stream()
        .map(this::toResponse)
        .toList();
  }

  @Override
  @Transactional
  public ChannelDto.Response update(UUID id, ChannelDto.UpdateRequest request) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ErrorCode.CHANNEL_NOT_FOUND));

    if (channel.getType() == ChannelType.PRIVATE) {
      throw new BusinessException(ErrorCode.PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED);
    }

    channel.update(request.newName(), request.newDescription());
    return toResponse(channel);
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    if (!channelRepository.existsById(id)) {
      throw new BusinessException(ErrorCode.CHANNEL_NOT_FOUND);
    }
    messageRepository.deleteByChannelId(id);
    readStatusRepository.deleteByChannelId(id);

    channelRepository.deleteById(id);
  }
}