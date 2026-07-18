package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationRequiredEventListener {

  private final ReadStatusRepository readStatusRepository;
  private final NotificationRepository notificationRepository;

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void on(MessageCreatedEvent event) {
    log.debug("메시지 알림 생성 시작: channelId={}", event.channelId());

    List<Notification> notifications = readStatusRepository
        .findAllByChannelIdAndNotificationEnabledTrue(event.channelId()).stream()
        .map(readStatus -> readStatus.getUser().getId())
        .filter(receiverId -> !receiverId.equals(event.authorId()))
        .map(receiverId -> Notification.builder()
            .receiverId(receiverId)
            .title(event.authorName() + " (#" + event.channelName() + ")")
            .content(event.content())
            .build())
        .toList();

    if (notifications.isEmpty()) {
      log.debug("알림 대상자 없음: channelId={}", event.channelId());
      return;
    }

    notificationRepository.saveAll(notifications);
    log.info("메시지 알림 생성 완료: channelId={}, 총 {}건", event.channelId(), notifications.size());
  }

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void on(RoleUpdatedEvent event) {
    log.debug("권한 변경 알림 생성 시작: userId={}", event.userId());

    notificationRepository.save(Notification.builder()
        .receiverId(event.userId())
        .title("권한이 변경되었습니다.")
        .content(event.oldRole() + " -> " + event.newRole())
        .build());

    log.info("권한 변경 알림 생성 완료: userId={}", event.userId());
  }
}