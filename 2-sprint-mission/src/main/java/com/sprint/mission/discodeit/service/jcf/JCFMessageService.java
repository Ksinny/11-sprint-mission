package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;

    public JCFMessageService() {
        this.data = new HashMap<>();
    }

    @Override
    public void create(Message message) {
        // 중복 생성 방지
        if (data.containsKey(message.getId())) {
            System.out.println("이미 존재하는 메시지 ID입니다.");
            return;
        }
        data.put(message.getId(), message);
        System.out.println("메시지가 전송 되었습니다.");
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
            System.out.println("메시지 내용이 수정되었습니다.");
        } else {
            System.out.println("해당 메시지를 찾을 수 없습니다.");
        }
    }

    @Override
    public void delete(UUID id) {
        Message removedMessage = data.remove(id);
        if (removedMessage != null) {
            System.out.println(id + " 메시지가 정상적으로 삭제되었습니다.");
        } else {
            System.out.println("해당 메시지를 찾을 수 없습니다.");
        }
    }
}