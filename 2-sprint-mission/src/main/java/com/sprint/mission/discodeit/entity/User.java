package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseUpdatableEntity {

  private String username;
  //  미사용 필드 주석처리
//  private String nickname;
//  private String description;
  private String email;
  private String password;

  private BinaryContent profile;
  private UserStatus status;

  @Builder
  public User(String username, String email, String password, BinaryContent profile,
      UserStatus status) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
    this.status = status;
  }

  public void changeUsername(String newUsername) {
    this.username = newUsername;
  }

  public void changeEmail(String newEmail) {
    this.email = newEmail;
  }

  public void changePassword(String newPassword) {
    this.password = newPassword;
  }

  // 프로필 이미지 수정
  public void updateProfileImage(BinaryContent newProfile) {
    if (newProfile != null && !newProfile.equals(this.profile)) {
      this.profile = newProfile;
    }
  }

  // 비밀번호 검증
  // 비밀번호 암호화는 Spring Security의 PasswordEncoder로 스프린트 미션에 맞추어 이후 진행 예정
  public void validatePassword(String password) {
    if (this.password == null || !this.password.equals(password)) {
      throw new IllegalArgumentException("Invalid username or password");
    }
  }
}
