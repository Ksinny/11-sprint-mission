package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileBinaryContentRepository() {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", BinaryContent.class.getSimpleName());
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

    private void saveToFile(BinaryContent binaryContent) {
        Path path = resolvePath(binaryContent.getId());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(binaryContent);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + path, e);
        }
    }

    private BinaryContent loadFromFile(Path path) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            return (BinaryContent) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to read file: " + path, e);
        }
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        saveToFile(binaryContent);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        Path path = resolvePath(id);
        if (Files.notExists(path)) {
            return Optional.empty();
        }
        return Optional.of(loadFromFile(path));
    }

    @Override
    public List<BinaryContent> findAll() {
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
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + path, e);
        }
    }
}