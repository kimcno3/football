package com.flab.football.controller;

import com.flab.football.controller.request.CreateChannelRequest;
import com.flab.football.controller.request.HealthCheckRequest;
import com.flab.football.controller.request.InviteParticipantsRequest;
import com.flab.football.controller.request.SendMessageRequest;
import com.flab.football.controller.response.ResponseDto;
import com.flab.football.service.chat.ChatService;
import com.flab.football.service.security.SecurityService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 채팅 기능 관련 API 선언이 되어있는 컨트롤러.
 */

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

  private final ChatService chatService;

  private final SecurityService securityService;

  /**
   * 웹 소켓 서버에 대한 healthCheck 값을 받기 위한 API.
   */

  @PostMapping("/health/check")
  public ResponseDto healthCheck(@RequestBody HealthCheckRequest request) {

    // 받아온 결과값을 저장하고 유지한다.
    chatService.healthCheck(
        request.getAddress(),
        request.getConnectionCount(),
        request.getHeartBeatTime()
    );

    return new ResponseDto(true, null, "헬스 체크 완료", null);

  }

  /**
   * 새로운 사용자를 웹소켓 서버에 연결시켜주는 API
   * req 포함 내용 ->
   */

  @GetMapping("/connect")
  public ResponseDto connectWebSocket() {

    // 가장 커넥션 수가 적은 서버 주소로 웹소켓 연결 요청을 보낸다.
    // user 정보를 보내줘야 할 수도 있겠다.
    chatService.connectUserToWebSocket();

    return new ResponseDto(true, null, "웹 소켓 연결 완료.", null);

  }


  /**
   * 새로운 채팅방 생성 API.
   */

  @PostMapping
  @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
  public ResponseDto createChannel(@Valid @RequestBody CreateChannelRequest request) {

    chatService.createChannel(request.getName());

    return new ResponseDto(true, null, "채팅방 생성 완료", null);

  }

  /**
   * 특정 채팅방에 참가자 초대 API.
   * 초대할 수 있는 참가자는 다수가 될 수 있습니다.
   */

  @PostMapping("/{channelId}")
  @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
  public ResponseDto inviteParticipants(@PathVariable(value = "channelId") int channelId,
                                        @RequestBody InviteParticipantsRequest request) {

    chatService.inviteParticipants(channelId, request.getParticipants());

    return new ResponseDto(true, null, "참가자 초대 완료", null);

  }

  /**
   * 로그인한 유저가 포함된 채팅방 목록을 조회 API.
   */

  @GetMapping
  public ResponseDto findChannels() {

    return new ResponseDto(true, null, "채팅방 조회", null);

  }

  /**
   * 메시지 수신자를 대상으로 메세지 또는 푸시알림 전송 API.
   */

  @PostMapping("/send/message")
  public ResponseDto sendMessage(@RequestBody SendMessageRequest request) {

    // 메세지를 보내는 사용자 id 조회
    int sendUserId = securityService.getCurrentUserId();

    // 아래 로직이 모두 ChatService.sendMessage() 로 가야한다.
    chatService.sendMessage(request.getChannelId(), sendUserId, request.getContent());

    return new ResponseDto<>(true, null, "분류 완료.", null);

  }

}
