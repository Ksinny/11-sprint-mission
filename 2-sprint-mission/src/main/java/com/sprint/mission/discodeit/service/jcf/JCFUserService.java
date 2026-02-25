package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data;

    public JCFUserService() {
        this.data = new HashMap<>();
    }

    @Override
    public void create(User user) {
        // 중복 생성 방지
        if (data.containsKey(user.getId())) {
            System.out.println("이미 존재하는 유저 ID입니다.");
            return;
        }
        data.put(user.getId(), user);
        System.out.println(user.getUserName() + " 유저가 생성되었습니다.");
    }

    @Override
    public User findById(UUID id) {
        return data.get(id);
    }

    @Override
    public Collection<User> findAll() {
        return data.values();
    }

    @Override
    public void update(UUID id, String userName, String nickname, String description, String email, String profileImage) {
        User user = data.get(id);
        if (user != null) {
            user.update(userName, nickname, description, email, profileImage);
            System.out.println(userName + " 유저 정보가 수정되었습니다.");
        } else {
            System.out.println("해당 유저를 찾을 수 없습니다.");
        }
    }

    @Override
    public void delete(UUID id) {
        User removedUser = data.remove(id);
        if (removedUser != null) {
            System.out.println("유저가 정상적으로 삭제되었습니다.");
        } else {
            System.out.println("해당 유저를 찾을 수 없습니다.");
        }
    }
}