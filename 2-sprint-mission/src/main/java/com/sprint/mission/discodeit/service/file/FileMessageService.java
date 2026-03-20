package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {
    private static final String FILE_PATH = "messages.ser";
    private final Map<UUID, Message> data;
    private final UserService userService;
    private final ChannelService channelService;

    // 의존성 주입
    public FileMessageService(UserService userService, ChannelService channelService) {
        this.data = load();
        this.userService = userService;
        this.channelService = channelService;
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
    private Map<UUID, Message> load() {
        File file = new File(FILE_PATH);

        // 파일 검증
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("파일 불러오기 실패");
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public Message create(String content, UUID authorId, UUID channelId) {
        userService.findById(authorId); // 유저 검증
        Channel channel = channelService.findById(channelId); // 채널 검증

        // 채널 접근 권한 검증
        if (channel.getType() == ChannelType.PRIVATE || channel.getType() == ChannelType.DM) {
            if (channel.getMemberIds() == null || !channel.getMemberIds().contains(authorId)) {
                throw new IllegalArgumentException("User " + authorId + " is not a member of channel " + channelId);
            }
        }

        Message message = new Message(content, authorId, channelId);
        data.put(message.getId(), message);
        save();
        return message;
    }

    @Override
    public Message findById(UUID id) {
        return Optional.ofNullable(data.get(id))
                .orElseThrow(() -> new NoSuchElementException("Message with id " + id + " not found"));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Message update(UUID id, String content) {
        Message message = findById(id);
        message.update(content);
        save();
        return message;
    }

    @Override
    public void delete(UUID id) {
        findById(id);
        data.remove(id);
        save();
    }
}