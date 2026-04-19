package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel extends BaseUpdatableEntity {

  private ChannelType type;
  private String name;
  private String description;

  @Builder
  private Channel(ChannelType type, String name, String description) {
    this.type = type;
    this.name = name;
    this.description = description;
  }

  // PUBLIC 채널 생성 메서드
  public static Channel createPublic(String name, String description) {
    return Channel.builder()
        .type(ChannelType.PUBLIC)
        .name(name)
        .description(description)
        .build();
  }

  // PRIVATE 채널 생성 메서드
  public static Channel createPrivate() {
    return Channel.builder()
        .type(ChannelType.PRIVATE)
        .build();
  }

  public void update(String newName, String newDescription) {
    if (newName != null && !newName.equals(this.name)) {
      this.name = newName;
    }
    if (newDescription != null && !newDescription.equals(this.description)) {
      this.description = newDescription;
    }
  }
}