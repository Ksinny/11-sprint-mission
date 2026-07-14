package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.JwtInformation;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
public class InMemoryJwtRegistry implements JwtRegistry {

  // <userId, Queue<JwtInformation>>
  private final Map<UUID, Queue<JwtInformation>> origin = new ConcurrentHashMap<>();
  private final int maxActiveJwtCount;

  public InMemoryJwtRegistry(int maxActiveJwtCount) {
    this.maxActiveJwtCount = maxActiveJwtCount;
  }

  @Override
  public void registerJwtInformation(JwtInformation jwtInformation) {
    UUID userId = jwtInformation.getUserDto().id();

    origin.compute(userId, (key, queue) -> {
      Queue<JwtInformation> targetQueue = (queue == null) ? new ConcurrentLinkedQueue<>() : queue;
      targetQueue.offer(jwtInformation);

      while (targetQueue.size() > maxActiveJwtCount) {
        targetQueue.poll();
      }
      return targetQueue;
    });
  }

  @Override
  public void invalidateJwtInformationByUserId(UUID userId) {
    origin.remove(userId);
  }

  @Override
  public boolean hasActiveJwtInformationByUserId(UUID userId) {
    Queue<JwtInformation> queue = origin.get(userId);
    return queue != null && !queue.isEmpty();
  }

  @Override
  public boolean hasActiveJwtInformationByAccessToken(String accessToken) {
    return origin.values().stream()
        .flatMap(Queue::stream)
        .anyMatch(info -> info.getAccessToken().equals(accessToken));
  }

  @Override
  public boolean hasActiveJwtInformationByRefreshToken(String refreshToken) {
    return origin.values().stream()
        .flatMap(Queue::stream)
        .anyMatch(info -> info.getRefreshToken().equals(refreshToken));
  }

  @Override
  public JwtInformation rotateJwtInformation(String oldRefreshToken,
      JwtInformation newJwtInformation) {
    origin.values().forEach(queue ->
        queue.removeIf(info -> info.getRefreshToken().equals(oldRefreshToken)));
    registerJwtInformation(newJwtInformation);
    return newJwtInformation;
  }

  @Scheduled(fixedDelayString = "${discodeit.jwt.cleanup-interval}")
  @Override
  public void clearExpiredJwtInformation() {
    origin.keySet().forEach(userId ->
        origin.computeIfPresent(userId, (key, queue) -> {
          queue.removeIf(JwtInformation::isExpired);
          return queue.isEmpty() ? null : queue;
        })
    );
    log.debug("만료 JWT 정리 완료");
  }
}