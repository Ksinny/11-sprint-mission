package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository {
    private static final String FILE_PATH = "channel.ser";
    private final Map<UUID, Channel> data;

    public FileChannelRepository() {
        this.data = loadFile();
    }

    private void saveFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
            System.out.println("파일 저장 완료: " + FILE_PATH);
        } catch (IOException e) {
            System.out.println("파일 저장 실패" + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, Channel> loadFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public void save(Channel channel) {
        data.put(channel.getId(), channel);
        saveFile();
    }

    @Override
    public Channel findById(UUID id) {
        return data.get(id);
    }

    @Override
    public Collection<Channel> findAll() {
        return data.values();
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
        saveFile();
    }
}