package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.dto.NotificationDto;
import com.sprint.mission.discodeit.event.NotificationCreatedEvent;
import com.sprint.mission.discodeit.service.SseService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class SseRequiredEventListener {

  private final SseService sseService;

  @TransactionalEventListener
  public void on(NotificationCreatedEvent event) {
    NotificationDto.Response notification = event.notification();

    sseService.send(Set.of(notification.receiverId()), "notifications.created", notification);
    log.debug("SSE 알림 전송: receiverId={}", notification.receiverId());
  }
}