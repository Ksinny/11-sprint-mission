package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.util.List;
import java.util.UUID;

public class JavaApplication {

    static User setupUser(UserService userService) {
        User user = new User("woody", "우디", "안녕", "woody@codeit.com", "password", "profile.png");
        userService.create(user);
        return user;
    }

    static Channel setupChannel(ChannelService channelService) {
        Channel channel = new Channel(ChannelType.PUBLIC, "공지", "", List.of());
        channelService.create(channel);
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        Message message = new Message("안녕하세요.", author.getId(), channel.getId());
        messageService.create(message);
        System.out.println("메시지 생성 완료: " + message.getId());
    }

    public static void main(String[] args) {
        // 서비스 초기화 및 의존성 주입
        FileUserRepository userRepository = new FileUserRepository();
        FileChannelRepository channelRepository = new FileChannelRepository();
        FileMessageRepository messageRepository = new FileMessageRepository();

        UserService userService = new BasicUserService(userRepository);
        ChannelService channelService = new BasicChannelService(channelRepository);
        MessageService messageService = new BasicMessageService(messageRepository, userService, channelService);

        System.out.println("========== 기본 셋업 테스트 ==========");
        User woody = setupUser(userService);
        Channel publicChannel = setupChannel(channelService);
        messageCreateTest(messageService, publicChannel, woody);


        System.out.println("\n========== 유저 테스트 ==========");
        User user1 = new User("이경신", "경신", "안녕하세요.", "dosly2@nave.com", "password" , "profile.png");
        User user2 = new User("김경신", "경신", "스프링 공부중입니다.", "dosly2@gmail.com", "password" , "img.png");

        System.out.println("---------- 유저 등록 ----------");
        userService.create(user1);
        userService.create(user2);

        System.out.print("유저 중복 가입 시도: ");
        userService.create(user2);

        System.out.println("---------- 유저 조회 ----------");
        System.out.println("유저 단건 조회: " + userService.findById(user1.getId()).getUserName());
        System.out.println("유저 전체 조회 수: " + userService.findAll().size() + "명");

        System.out.println("---------- 유저 수정 ----------");
        userService.update(user2.getId(), "김경신2", "경신2", "스프링 개발중입니다.", "dosly2@naver.com", "new_img.png");

        System.out.println("---------- 유저 삭제 ----------");
        userService.delete(user1.getId());
        System.out.print("유저 중복 삭제 시도: ");
        userService.delete(user1.getId());


        System.out.println("\n========== 채널 테스트 ==========");
        Channel channel1 = new Channel(ChannelType.PUBLIC, "코드잇 SB 11기", "Spring BackEnd", List.of());
        Channel channel2 = new Channel(ChannelType.PRIVATE, "SB_2팀", "SB 11기 2팀 전용 채널", List.of(user2.getId()));

        System.out.println("---------- 채널 등록 ----------");
        channelService.create(channel1);
        channelService.create(channel2);

        System.out.print("채널 중복 생성 시도: ");
        channelService.create(channel2);

        System.out.println("---------- 채널 조회 ----------");
        System.out.println("채널 단건 조회: " + channelService.findById(channel1.getId()).getName());
        System.out.println("채널 전체 조회 수: " + channelService.findAll().size() + "개");

        System.out.println("---------- 채널 수정 ----------");
        channelService.update(channel1.getId(), ChannelType.PUBLIC, "코드잇_SB_11기 단체 채널", List.of(user2.getId()));

        System.out.println("---------- 채널 삭제 ----------");
        channelService.delete(channel2.getId());
        System.out.print("채널 중복 삭제 시도: ");
        channelService.delete(channel2.getId());


        System.out.println("\n========== 메시지 테스트 ==========");
        Message message1 = new Message("안녕하세요!", user2.getId(), channel1.getId());
        Message message2 = new Message("잘 부탁드려요", user2.getId(), channel1.getId());

        System.out.println("---------- 메시지 등록 ----------");
        messageService.create(message1);
        messageService.create(message2);

        System.out.print("메시지 중복 전송 시도: ");
        messageService.create(message2);

        System.out.println("---------- 메시지 조회 ----------");
        System.out.println("메시지 단건 내용 조회: " + messageService.findById(message1.getId()).getContent());
        System.out.println("메시지 전체 조회 수: " + messageService.findAll().size() + "개");

        System.out.println("---------- 메시지 수정 ----------");
        messageService.update(message1.getId(), "안녕하세요! 이경신입니다.");

        System.out.println("---------- 메시지 삭제 ----------");
        messageService.delete(message2.getId());
        System.out.print("메시지 중복 삭제 시도: ");
        messageService.delete(message2.getId());


        System.out.println("\n========== 심화 검증 테스트 ==========");
        User teacher = new User("주강사", "강사", "SB 코스 강사입니다.", "wnrkdtk@naver.com","password" , "hello.png");
        userService.create(teacher);

        Channel privateChannel = new Channel(ChannelType.PRIVATE, "비밀 채팅방", "비밀 채팅방입니다.", List.of());
        channelService.create(privateChannel);

        System.out.print("등록된 유저가 아닐 시 -> ");
        Message testMessage1 = new Message("이곳은 비밀 채널입니다.", UUID.randomUUID(), privateChannel.getId());
        messageService.create(testMessage1);

        System.out.print("존재하는 채널이 아닐 시 -> ");
        Message testMessage2 = new Message("이곳은 비밀 채널입니다.", teacher.getId(), UUID.randomUUID());
        messageService.create(testMessage2);

        System.out.print("채널 멤버가 아닐 시 -> ");
        Message testMessage3 = new Message("이곳은 비밀 채널입니다.", teacher.getId(), privateChannel.getId());
        messageService.create(testMessage3);
    }
}