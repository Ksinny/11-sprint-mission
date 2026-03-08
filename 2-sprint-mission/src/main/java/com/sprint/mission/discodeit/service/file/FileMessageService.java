package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.UUID;

public class FileMessageService implements MessageService {
    private static final String FILE_PATH = "message.ser";
    private final Map<UUID, Message> data;

    private final UserService userService;
    private final ChannelService channelService;

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
    public void create(Message message) {
        // 유저 검증
        if (userService.findById(message.getSenderId()) == null) {
            System.out.println("존재하지 않는 유저입니다. 메시지 전송 실패");
            return;
        }

        // 채널 검증
        Channel channel = channelService.findById(message.getChannelId());
        if (channelService.findById(message.getChannelId()) == null) {
            System.out.println("존재하지 않는 채널입니다. 메시지 전송 실패");
            return;
        }

        // 채널 멤버 검증
        if (channel.getType() == ChannelType.PRIVATE || channel.getType() == ChannelType.DM) {
            if (!channel.getMemberIds().contains(message.getSenderId())) {
                System.out.println("해당 채널의 멤버가 아닙니다. 메시지 전송 실패");
                return;
            }
        }

        // 중복 생성 방지
        if (data.containsKey(message.getId())) {
            System.out.println("이미 존재하는 메시지입니다. 메시지 전송 실패");
            return;
        }
        data.put(message.getId(), message);
        System.out.println("메시지가 전송 되었습니다.");

        // 파일에 저장
        save();
    }

    @Override
    public Message findById(UUID id) {
        return data.get(id);
    }

    @Override
    public Collection<Message> findAll() {
        return data.values();
    }

    @Override
    public void update(UUID id, String content) {
        Message message = data.get(id);
        if (message != null) {
            message.update(content);
            System.out.println(content + "로 메시지가 수정되었습니다.");

            // 파일에 저장
            save();
        } else {
            System.out.println("해당 메시지를 찾을 수 없습니다.");
        }
    }

    @Override
    public void delete(UUID id) {
        Message removedMessage = data.remove(id);
        if (removedMessage != null) {
            System.out.println("메시지가 정상적으로 삭제되었습니다.");

            // 파일에 저장
            save();
        } else {
            System.out.println("해당 메시지를 찾을 수 없습니다.");
        }
    }
}