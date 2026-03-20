package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileUserRepository implements UserRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileUserRepository() {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", User.class.getSimpleName());
        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory: " + DIRECTORY, e);
            }
        }
    }

    private Path resolvePath(UUID id) {
        return DIRECTORY.resolve(id + EXTENSION);
    }

    private void saveToFile(User user) {
        Path path = resolvePath(user.getId());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + path, e);
        }
    }

    private User loadFromFile(Path path) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            return (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to read file: " + path, e);
        }
    }

    @Override
    public User save(User user) {
        saveToFile(user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        Path path = resolvePath(id);
        if (Files.notExists(path)) {
            return Optional.empty();
        }
        return Optional.of(loadFromFile(path));
    }

    @Override
    public List<User> findAll() {
        try (var pathStream = Files.list(DIRECTORY)) {
            return pathStream
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(this::loadFromFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read directory: " + DIRECTORY, e);
        }
    }

    @Override
    public boolean existsById(UUID id) {
        return Files.exists(resolvePath(id));
    }

    @Override
    public void deleteById(UUID id) {
        Path path = resolvePath(id);
        try {
            Files.deleteIfExists(path); // 존재할 때에만 삭제
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + path, e);
        }
    }
}