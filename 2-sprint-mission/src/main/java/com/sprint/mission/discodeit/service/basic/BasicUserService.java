package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;


    @Override
    public UserDto.Response create(UserDto.CreateRequest request) {
        // username 중복 확인
        if (userRepository.existsByName(request.userName())) {
            throw new IllegalArgumentException("User with name " + request.userName() + " already exists");
        }

        // email 중복 확인
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("User with email " + request.email() + " already exists");
        }

        // toEntity()로 유저 등록
        User user = request.toEntity();
        userRepository.save(user);

        // UserStatus 함께 생성
        UserStatus userStatus = new UserStatus(user.getId());
        userStatusRepository.save(userStatus);

        return UserDto.Response.of(user, userStatus);
    }

    @Override
    public UserDto.Response findById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found"));

        UserStatus userStatus = userStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found"));

        return UserDto.Response.of(user, userStatus);
    }

    @Override
    public List<UserDto.Response> findAll() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> {
                    UserStatus status = userStatusRepository.findById(user.getId())
                            .orElseGet(() -> new UserStatus(user.getId()));
                    return UserDto.Response.of(user, status);
                })
                .toList();
    }

    @Override
    public User update(UUID id, String userName, String nickname, String description, String email, String password, UUID profileImageId) {
/*        User user = findById(id);
        user.update(userName, nickname, description, email, password);
        user.updateProfileImage(profileImageId);
        return userRepository.save(user);*/
        return null;
    }

    @Override
    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }
}