package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    // 의존성 주입
    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(String userName, String nickname, String description, String email, String password, String profileImage) {
        User user = new User(userName, nickname, description, email, password, profileImage);

        // 유저 저장
        userRepository.save(user);
        return user;
    }

    @Override
    public User findById(UUID id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new NoSuchElementException("User with id " + id + " not found");
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userRepository.findAll());
    }

    @Override
    public User update(UUID id, String userName, String nickname, String description, String email, String password, String profileImage) {
        User user = findById(id);
        user.update(userName, nickname, description, email, password, profileImage);

        userRepository.save(user); // 수정된 유저 덮어쓰며 저장
        return user;
    }

    @Override
    public void delete(UUID id) {
        findById(id);

        userRepository.delete(id); // 유저 삭제
    }
}