package com.ssafy.jarviser.controller;

import com.ssafy.jarviser.dto.SessionUserDto;
import com.ssafy.jarviser.security.JwtService;
import com.ssafy.jarviser.util.AESEncryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"*"})
public class SocketController {
    private final JwtService jwtService;
    private final SimpMessagingTemplate messagingTemplate;
    private final AESEncryptionUtil aesEncryptionUtil;

    private final HashMap<Long, HashSet<SessionUserDto>> connectSessionMap = new HashMap<>(); //TODO: 추후 보완 필요
    private final HashMap<String, SessionUserDto> sessionUserMap = new HashMap<>();


    @MessageMapping(value = "/message")
    public void  getMessage(@Payload Map<String, Object> payload, SimpMessageHeaderAccessor headerAccessor){
        Map<String, String> resultMap = new HashMap<>();
        Date arriveDate = new Date();
        String arriveTime = arriveDate.getHours() + ":" + arriveDate.getMinutes() + ":" + arriveDate.getSeconds();

        String token = ((String) payload.get("Authorization")).split(" ")[1];
        Long meetingId = Long.parseLong((String) payload.get("meetingId"));
        String content = (String) payload.get("content");

        try {
            Long userId = jwtService.extractUserId(token);
            String userName = jwtService.extractUserName(token);

            resultMap.put("type", "chat");
            resultMap.put("userId", userId.toString());
            resultMap.put("userName", userName);
            resultMap.put("time", arriveTime);
            resultMap.put("content", content);

            messagingTemplate.convertAndSend("/topic/" + meetingId, resultMap);
        }catch (Exception e){
            log.error("error", e);
            resultMap.put("message", e.getMessage());

            resultMap.clear();
            resultMap.put("type", "error");
            resultMap.put("content", "메시지 전송에 실패했습니다.");

            messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/topic/" + meetingId, resultMap);
        }
    }

    @EventListener(SessionConnectedEvent.class)
    public void handleWebsocketConnectListener(SessionConnectedEvent event) {
        Map<String, Object> targetResultMap = new HashMap<>();
        Map<String, String> resultMap = new HashMap<>();
        Date arriveDate = new Date();
        String arriveTime = arriveDate.getHours() + ":" + arriveDate.getMinutes() + ":" + arriveDate.getSeconds();

        try {
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
            String sessionId = headerAccessor.getSessionId();
            String destination = headerAccessor.getDestination();
            String meetingId = destination.split("/")[2];
            Long mId = Long.parseLong(aesEncryptionUtil.decrypt(meetingId));

            String token = ((String) Objects.requireNonNull(headerAccessor.getFirstNativeHeader("Authorization"))).split(" ")[1];
            Long userId = jwtService.extractUserId(token);
            String userName = jwtService.extractUserName(token);

            if (!connectSessionMap.containsKey(mId)) {
                connectSessionMap.put(mId, new HashSet<>());
            }
            HashSet<SessionUserDto> participants = connectSessionMap.get(mId);

            //새로 접속한 인원에게 기존의 참가자 명단 보내주기
            targetResultMap.put("type", "participants");
            targetResultMap.put("content", participants);
            messagingTemplate.convertAndSendToUser(sessionId, destination, targetResultMap);

            //새로운 참가자 입장 알림
            SessionUserDto user = new SessionUserDto(userId, userName);
            participants.add(user);
            sessionUserMap.put(sessionId, user);

            resultMap.put("type", "connect");
            resultMap.put("userId", userId.toString());
            resultMap.put("userName", userName);
            resultMap.put("time", arriveTime);
            resultMap.put("content", userName + "님이 입장하셨습니다.");
            messagingTemplate.convertAndSend(destination, resultMap);
        } catch (Exception e) {
            log.error("error", e);
            resultMap.put("message", e.getMessage());
        }
    }

    @EventListener(SessionDisconnectEvent.class)
    public void handleWebSocketDisconnectedListener(SessionDisconnectEvent event) {
        Map<String, String> resultMap = new HashMap<>();
        Date arriveDate = new Date();
        String arriveTime = arriveDate.getHours() + ":" + arriveDate.getMinutes() + ":" + arriveDate.getSeconds();

        try {
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
            String sessionId = headerAccessor.getSessionId();
            String destination = headerAccessor.getDestination();
            String meetingId = destination.split("/")[2];
            Long mId = Long.parseLong(aesEncryptionUtil.decrypt(meetingId));

            String token = ((String) Objects.requireNonNull(headerAccessor.getFirstNativeHeader("Authorization"))).split(" ")[1];
            Long userId = jwtService.extractUserId(token);
            String userName = jwtService.extractUserName(token);

            //참가자 명단에서 제거
            HashSet<SessionUserDto> participants = connectSessionMap.get(mId);
            participants.remove(sessionUserMap.get(sessionId));
            sessionUserMap.remove(sessionId);
            if(participants.size() == 0){
                connectSessionMap.remove(mId);
            }

            //참가자 퇴장 알림
            resultMap.put("type", "disconnect");
            resultMap.put("userId", userId.toString());
            resultMap.put("userName", userName);
            resultMap.put("time", arriveTime);
            resultMap.put("content", userName + "님이 퇴장하셨습니다.");
            messagingTemplate.convertAndSend(destination, resultMap);
        } catch (Exception e) {
            log.error("error", e);
            resultMap.put("message", e.getMessage());
        }
    }

    /*
    //TODO: 추후 필요에 의해 추가 예정 핑퐁체크
    @MessageMapping(value = "/alive")
    public void getSessionAlive(@Payload Map<String, Object> payload, SimpMessageHeaderAccessor headerAccessor){
        boolean alive = payload.get("content").equals("alive");
        String sessionId = headerAccessor.getSessionId();
        if(alive){
            aliveSessionSet.add(sessionId);
            soonDieSessionSet.remove(sessionId);
        }
    }


    //TODO: 추후 필요에 의해 추가 예정 핑퐁체크
    @Scheduled(fixedDelay = 10000)
    public void checkAliveSession() {
        Date now = new Date();
        String arriveTime = now.getHours() + ":" + now.getMinutes() + ":" + now.getSeconds();
        HashMap<String, String> resultMap = new HashMap<>();

        for (String sessionId : soonDieSessionSet) {
            SessionUserDto user = sessionUserMap.get(sessionId);
            Long userId = user.getUserId();
            String userName = user.getUserName();
            //참가자 퇴장 알림
            resultMap.clear();
            resultMap.put("type", "disconnect");
            resultMap.put("userId", userId.toString());
            resultMap.put("userName", userName);
            resultMap.put("time", arriveTime);
            resultMap.put("content", userName + "님이 퇴장하셨습니다.");
            messagingTemplate.convertAndSend(se, resultMap);
        }
        soonDieSessionSet.clear();

        //교환
        HashSet<String> temp = soonDieSessionSet;
        soonDieSessionSet = aliveSessionSet;
        aliveSessionSet = temp;
    }
     */
}
