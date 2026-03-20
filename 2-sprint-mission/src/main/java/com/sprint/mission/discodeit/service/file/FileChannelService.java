package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {
    private static final String FILE_PATH = "channels.ser";
    private final Map<UUID, Channel> data;

    public FileChannelService() {
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
    private Map<UUID, Channel> load() {
        File file = new File(FILE_PATH);

        // 파일 검증
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("파일 불러오기 실패");
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public Channel create(ChannelType type, String name, String description, List<UUID> memberIds) {
        Channel channel = new Channel(type, name, description, memberIds);
        data.put(channel.getId(), channel);
        save();
        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        return Optional.ofNullable(data.get(id))
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + id + " not found"));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel update(UUID id, ChannelType type, String name, String description, List<UUID> memberIds) {
        Channel channel = findById(id);
        channel.update(type, name, description, memberIds);
        save();
        return channel;
    }

    @Override
    public void delete(UUID id) {
        findById(id);
        data.remove(id);
        save();
    }
}