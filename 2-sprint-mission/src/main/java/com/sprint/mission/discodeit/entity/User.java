package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {
    private String userName;
    private String nickname;
    private String description;
    private String email;
    private String profileImage;


    public User(String userName, String nickname, String description, String email, String profileImage) {
        super();
        this.userName = userName;
        this.nickname = nickname;
        this.description = description;
        this.email = email;
        this.profileImage = profileImage;
    }

    public String getUserName() {
        return userName;
    }

    public String getNickname() {
        return nickname;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void update(String userName, String nickname, String description, String email, String profileImage) {
        this.userName = userName;
        this.nickname = nickname;
        this.description = description;
        this.email = email;
        this.profileImage = profileImage;

        super.timeUpdate();
    }

    @Override
    public String toString() {
        return "User [" +
                "UUID: " + getId() +
                "\n이름: " + getUserName() +
                ", 별명: " + getNickname() +
                ", 소개: " + getDescription() +
                ", 이메일: " + getEmail() +
                ", 프로필 사진: " + getProfileImage() +
                ", 생성 시간: " + getCreatedAt() +
                ", 수정 시간: " + getUpdatedAt() +
                ']';
    }

}
