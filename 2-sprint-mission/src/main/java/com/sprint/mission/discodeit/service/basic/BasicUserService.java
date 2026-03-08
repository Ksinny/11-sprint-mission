package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Collection;
import java.util.UUID;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    // 의존성 주입
    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void create(User user) {
        // 유저 중복 검증
        if (userRepository.findById(user.getId()) != null) {
            System.out.println("이미 존재하는 유저 ID입니다.");
            return;
        }
        
        // 유저 저장
        userRepository.save(user);
        System.out.println(user.getUserName() + " 유저가 생성되었습니다.");
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void update(UUID id, String userName, String nickname, String description, String email, String profileImage) {
        User user = userRepository.findById(id);
        if (user != null) {
            user.update(userName, nickname, description, email, profileImage);
            userRepository.save(user); // 수정된 유저 덮어쓰며 저장
            System.out.println(userName + " 유저 정보가 수정되었습니다.");
        } else {
            System.out.println("해당 유저를 찾을 수 없습니다.");
        }
    }

    @Override
    public void delete(UUID id) {
        User user = userRepository.findById(id);
        if (user != null) {
            userRepository.delete(id); // 유저 삭제
            System.out.println("유저가 정상적으로 삭제되었습니다.");
        } else {
            System.out.println("해당 유저를 찾을 수 없습니다.");
        }
    }
}