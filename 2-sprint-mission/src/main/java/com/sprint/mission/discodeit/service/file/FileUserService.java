package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileUserService implements UserService {
    private static final String FILE_PATH = "users.ser";
    private final Map<UUID, User> data;

    public FileUserService() {
        this.data = load();
    }

    // 직렬화
    private void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
            System.out.println("파일 저장 완료: " + FILE_PATH);
        } catch (IOException e) {
            System.out.println("파일 저장 실패" + e.getMessage());
            e.printStackTrace();
        }
    }

    // 역직렬화
    @SuppressWarnings("unchecked") // 타입캐스팅 경고 무시
    private Map<UUID, User> load() {
        File file = new File(FILE_PATH);

        // 파일 검증
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("파일 불러오기 실패");
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public void create(User user) {
        if (data.containsKey(user.getId())) {
            System.out.println("이미 존재하는 유저 ID입니다.");
            return;
        }
        data.put(user.getId(), user);
        System.out.println(user.getUserName() + " 유저가 생성되었습니다.");

        // 파일에 저장
        save();
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

            // 파일에 저장
            save();
        } else {
            System.out.println("해당 유저를 찾을 수 없습니다.");
        }
    }

    @Override
    public void delete(UUID id) {
        User removedUser = data.remove(id);
        if (removedUser != null) {
            System.out.println("유저가 정상적으로 삭제되었습니다.");

            // 파일 저장
            save();
        } else {
            System.out.println("해당 유저를 찾을 수 없습니다.");
        }
    }
}