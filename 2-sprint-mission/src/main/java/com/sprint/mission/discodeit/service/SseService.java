package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.SseMessage;
import com.sprint.mission.discodeit.repository.SseEmitterRepository;
import com.sprint.mission.discodeit.repository.SseMessageRepository;
import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {

  private static final Duration TIMEOUT = Duration.ofMinutes(30);

  private final SseEmitterRepository sseEmitterRepository;
  private final SseMessageRepository sseMessageRepository;

  public SseEmitter connect(UUID receiverId, UUID lastEventId) {
    SseEmitter emitter = new SseEmitter(TIMEOUT.toMillis());

    emitter.onCompletion(() -> sseEmitterRepository.remove(receiverId, emitter));
    emitter.onTimeout(() -> sseEmitterRepository.remove(receiverId, emitter));
    emitter.onError(throwable -> sseEmitterRepository.remove(receiverId, emitter));

    sseEmitterRepository.save(receiverId, emitter);

    if (!ping(emitter)) {
      sseEmitterRepository.remove(receiverId, emitter);
      log.warn("SSE 최초 연결 실패: receiverId={}", receiverId);
      return emitter;
    }

    if (lastEventId != null) {
      replay(receiverId, emitter, lastEventId);
    }

    log.info("SSE 연결 성공: receiverId={}", receiverId);
    return emitter;
  }

  public void send(Collection<UUID> receiverIds, String eventName, Object data) {
    SseMessage message = SseMessage.of(eventName, data, Set.copyOf(receiverIds));
    sseMessageRepository.save(message);

    receiverIds.forEach(receiverId ->
        sseEmitterRepository.findAllByReceiverId(receiverId)
            .forEach(emitter -> doSend(receiverId, emitter, message)));
  }

  public void broadcast(String eventName, Object data) {
    SseMessage message = SseMessage.broadcast(eventName, data);
    sseMessageRepository.save(message);

    sseEmitterRepository.findAll().forEach((receiverId, emitters) ->
        emitters.forEach(emitter -> doSend(receiverId, emitter, message)));
  }

  @Scheduled(fixedDelay = 1000 * 60 * 30)
  public void cleanUp() {
    sseEmitterRepository.findAll().forEach((receiverId, emitters) ->
        emitters.forEach(emitter -> {
          if (!ping(emitter)) {
            log.debug("만료된 SSE 연결 제거: receiverId={}", receiverId);
            sseEmitterRepository.remove(receiverId, emitter);
          }
        }));
  }

  private void replay(UUID receiverId, SseEmitter emitter, UUID lastEventId) {
    List<SseMessage> missed = sseMessageRepository.findAllAfter(lastEventId);
    missed.stream()
        .filter(message -> message.isReceivable(receiverId))
        .forEach(message -> doSend(receiverId, emitter, message));

    log.info("SSE 유실 이벤트 재전송: receiverId={}, {}건", receiverId, missed.size());
  }

  private void doSend(UUID receiverId, SseEmitter emitter, SseMessage message) {
    try {
      emitter.send(SseEmitter.event()
          .id(message.eventId().toString())
          .name(message.eventName())
          .data(message.data()));
    } catch (IOException | IllegalStateException e) {
      log.warn("SSE 전송 실패, 연결 제거: receiverId={}, eventName={}",
          receiverId, message.eventName(), e);
      sseEmitterRepository.remove(receiverId, emitter);
    }
  }

  private boolean ping(SseEmitter emitter) {
    try {
      emitter.send(SseEmitter.event().comment("ping"));
      return true;
    } catch (IOException | IllegalStateException e) {
      log.debug("SSE ping 실패", e);
      return false;
    }
  }
}