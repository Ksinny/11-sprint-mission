package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;

public class JavaApplication {

    public static void main(String[] args) {


        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService();

        System.out.println("========== 유저  테스트 ==========");
        User user1 = new User("이경신", "경신", "안녕하세요.", "dosly2@nave.com", "profile.png");
        User user2 = new User("김경신", "경신", "스프링 공부중입니다.", "dosly2@gmail.com", "img.png");

        System.out.println("---------- 유저  등록 ----------");
        userService.create(user1);
        userService.create(user2);

        // 유저 중복 가입 테스트
        userService.create(user2);

        System.out.println("---------- 유저  조회 ----------");
        System.out.println("유저 단건 조회: \n" + userService.findById(user1.getId()));

        System.out.println("유저 전체 조회: \n" + userService.findAll());

        System.out.println("---------- 유저  수정 ----------");
        userService.update(user2.getId(), "김경신2", "경신2", "스프링 개발중입니다.", "dosly2@naver.com", "new_img.png");

        System.out.println("---------- 유저  삭제 ----------");
        userService.delete(user1.getId());
        userService.delete(user1.getId());

        System.out.println("\n========== 채널 테스트 ==========");
        Channel channel1 = new Channel(ChannelType.PUBLIC, "코드잇 SB 11기", List.of());
        Channel channel2 = new Channel(ChannelType.PRIVATE, "SB_2팀", List.of(user2.getId()));

        System.out.println("---------- 채널 등록 ----------");
        channelService.create(channel1);
        channelService.create(channel2);

        // 채널 중복 생성 테스트
        channelService.create(channel2);

        System.out.println("---------- 채널 조회 ----------");
        System.out.println("채널 단건 조회: \n" + channelService.findById(channel1.getId()));
        System.out.println("채널 전체 조회: \n" + channelService.findAll());

        System.out.println("---------- 채널 수정 ----------");
        channelService.update(channel1.getId(), ChannelType.PRIVATE, "코드잇_SB_11기", List.of(user1.getId()));

        System.out.println("---------- 채널 삭제 ----------");
        channelService.delete(channel2.getId());
        channelService.delete(channel2.getId());


        System.out.println("\n========== 메시지 테스트 ==========");
        Message message1 = new Message("안녕하세요!", user2.getId(), channel1.getId());
        Message message2 = new Message("잘 부탁드려요", user2.getId(), channel1.getId());

        System.out.println("---------- 메시지 등록 ----------");
        messageService.create(message1);
        messageService.create(message2);

        // 메시지 중복 전송 테스트
        messageService.create(message2);

        System.out.println("---------- 메시지 조회 ----------");
        System.out.println("메시지 단건 조회: \n" + messageService.findById(message1.getId()));
        System.out.println("메시지 전체 조회: \n" + messageService.findAll());

        System.out.println("---------- 메시지 수정 ----------");
        messageService.update(message1.getId(), "안녕하세요! 이경신입니다.");

        System.out.println("---------- 메시지 삭제 ----------");
        messageService.delete(message2.getId());
        messageService.delete(message2.getId());
    }
}
