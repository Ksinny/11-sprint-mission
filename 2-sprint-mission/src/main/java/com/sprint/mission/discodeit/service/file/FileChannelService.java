package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {
    private static final String FILE_PATH = "channel.ser";
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
    public void create(Channel channel) {
        // 중복 생성 방지
        if (data.containsKey(channel.getId())) {
            System.out.println("이미 존재하는 채널 ID입니다.");
            return;
        }
        data.put(channel.getId(), channel);
        System.out.println(channel.getName() + " 채널이 생성되었습니다.");

        // 파일에 저장
        save();
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
    public void update(UUID id, ChannelType type, String name, List<UUID> memberIds) {
        Channel channel = data.get(id);
        if (channel != null) {
            channel.update(type, name, memberIds);
            System.out.println(name + " 채널 정보가 수정되었습니다.");

            // 파일에 저장
            save();
        } else {
            System.out.println("해당 채널을 찾을 수 없습니다.");
        }
    }

    @Override
    public void delete(UUID id) {
        Channel removedChannel = data.remove(id);
        if (removedChannel != null) {
            System.out.println("채널이 정상적으로 삭제되었습니다.");

            // 파일에 저장
            save();
        } else {
            System.out.println("해당 채널을 찾을 수 없습니다.");
        }
    }
}