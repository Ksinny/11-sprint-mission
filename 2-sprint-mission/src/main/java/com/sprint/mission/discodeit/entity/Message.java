package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseUpdatableEntity {

  private String content;
  private User author;
  private Channel channel;
  private List<UUID> attachmentIds;


  @Builder
  public Message(String content, User author, Channel channel, List<UUID> attachmentIds) {
    this.content = content;
    this.author = author;
    this.channel = channel;
    this.attachmentIds = attachmentIds != null ? new ArrayList<>(attachmentIds) : new ArrayList<>();
  }

  // 메시지 내용만 수정 가능
  public void update(String newContent) {
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
    }
  }

  // 첨부파일 추가
  public void addAttachment(UUID attachmentId) {
    this.attachmentIds.add(attachmentId);
  }
}