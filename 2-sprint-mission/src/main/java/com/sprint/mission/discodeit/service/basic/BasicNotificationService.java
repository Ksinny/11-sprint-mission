package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicNotificationService implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;

  @Override
  @Cacheable(cacheNames = "notifications", key = "#receiverId")
  public List<NotificationDto.Response> findAllByReceiverId(UUID receiverId) {
    log.debug("알림 목록 조회 시작: receiverId={}", receiverId);

    List<NotificationDto.Response> responses =
        notificationRepository.findAllByReceiverIdOrderByCreatedAtDesc(receiverId).stream()
            .map(notificationMapper::toDto)
            .toList();

    log.info("알림 목록 조회 완료: receiverId={}, 총 {}건", receiverId, responses.size());
    return responses;
  }

  @Override
  @Transactional
  @CacheEvict(cacheNames = "notifications", key = "#requesterId")
  public void delete(UUID id, UUID requesterId) {
    log.debug("알림 삭제 시작: id={}", id);

    Notification notification = notificationRepository.findById(id)
        .orElseThrow(() -> NotificationNotFoundException.withId(id));

    if (!notification.getReceiverId().equals(requesterId)) {
      log.warn("타인의 알림 삭제 시도: notificationId={}, requesterId={}", id, requesterId);
      throw new AccessDeniedException("본인의 알림만 삭제할 수 있습니다.");
    }

    notificationRepository.delete(notification);
    log.info("알림 삭제 완료: id={}", id);
  }
}