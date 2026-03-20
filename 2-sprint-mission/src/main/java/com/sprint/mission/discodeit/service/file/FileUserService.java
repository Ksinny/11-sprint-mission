package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

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
    public User create(String userName, String nickname, String description, String email, String password, String profileImage) {
        User user = new User(userName, nickname, description, email, password, profileImage);
        data.put(user.getId(), user);
        save();
        return user;
    }

    @Override
    public User findById(UUID id) {
        return Optional.ofNullable(data.get(id))
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found"));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User update(UUID id, String userName, String nickname, String description, String email, String password, String profileImage) {
        User user = findById(id);
        user.update(userName, nickname, description, email, password, profileImage);
        save();
        return user;
    }

    @Override
    public void delete(UUID id) {
        findById(id);
        data.remove(id);
        save();
    }
}