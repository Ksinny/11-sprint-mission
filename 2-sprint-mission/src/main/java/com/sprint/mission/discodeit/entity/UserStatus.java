package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStatus extends BaseUpdatableEntity {

  private UUID userId;
  private Instant lastActiveAt;

  @Builder
  public UserStatus(UUID userId, Instant lastActiveAt) {
    this.userId = userId;
    this.lastActiveAt = lastActiveAt != null ? lastActiveAt : Instant.now();
  }

  // 온라인 여부 확인 메서드
  public boolean isOnline() {
    // 접속 시간 5분 이내: 온라인
    return lastActiveAt.isAfter(Instant.now().minusSeconds(300));
  }

  public void updateActiveTime(Instant lastActiveAt) {
    if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
      this.lastActiveAt = lastActiveAt;
    }
  }
}