package com.ssafy.jarviser.controller;

import com.nimbusds.jose.shaded.gson.Gson;
import com.ssafy.jarviser.security.JwtService;
import com.ssafy.jarviser.service.OpenAIService;
import com.ssafy.jarviser.util.AESEncryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"*"})
@RequestMapping("socket")
public class SocketController {
    private final JwtService jwtService;
    private final OpenAIService openAIService;
    private final SimpMessagingTemplate messagingTemplate;
    private final AESEncryptionUtil aesEncryptionUtil;
    private final Gson gson = new Gson();


    private Map<String, String> getDataFromToken(String token){
        Map<String, String> data = new HashMap<>();
        try {
            token = token.split(" ")[1];
            Long userId = jwtService.extractUserId(token);
            String userName = jwtService.extractUserName(token);
            data.put("userId", userId.toString());
            data.put("userName", userName);
        } catch (Exception e) {
            log.error("token 정보 얻기 실패: {}", e);
            throw new RuntimeException("token 정보 얻기 실패");
        }
        return data;
    }

    private String decryptMeetingId(String meetingId){
        try{
            meetingId = aesEncryptionUtil.decrypt(meetingId);
        }catch (Exception e){
            log.error("meetingId decrypt error : {}", e);
            throw new RuntimeException("meetingId decrypt error");
        }
        return meetingId;
    }

    private String saveAudio(String meetingId, long userId, MultipartFile file){
        String filePath = "audio/"+meetingId+"/"+userId+"/";
        try{
            // Audio 폴더에 가서 (없으면 생성)
            // meetingId의 폴더가 없으면 만들고
            // 그 하위에 userId 폴더가 없으면 만들고
            // 그 하위에 파일 저장
            //TODO: 추후 MultiPartFile을 File로 즉각 변환해본 후 성능 테스트해보기
            FileOutputStream fos = new FileOutputStream(filePath);
                // 파일 저장할 경로 + 파일명을 파라미터로 넣고 fileOutputStream 객체 생성하고
            InputStream is = file.getInputStream();){

            int readCount = 0;
            byte[] buffer = new byte[1024];

            while ((readCount = is.read(buffer)) != -1) {
                //  파일에서 가져온 fileInputStream을 설정한 크기 (1024byte) 만큼 읽고
                fos.write(buffer, 0, readCount);
                // 위에서 생성한 fileOutputStream 객체에 출력하기를 반복한다
                }
            }
            // 저장하는 이름은 userId + 현재 시간으로
        }catch (Exception e) {
            log.error("save audio error : {}", e);
            throw new RuntimeException("save audio error");
        }
        return filePath;
    }

    private String getStt(String filePath){
        String stt = "";
        try{
            String textResponse = openAIService.whisperAPICall(filePath).block();
            assert textResponse != null;
            stt = (String) gson.fromJson(textResponse, HashMap.class).get("text");
        }catch (Exception e){
            log.error("get stt error : {}", e);
            throw new RuntimeException("get stt error");
        }
        //FIXME: 이상한 값이 오는 것 같으면 처리해주는 로직 필요
        return stt;
    }

    private void socketSend(Map<String, String> responseMap, String meetingId){
        try{
            String response = gson.toJson(responseMap).toString();
            messagingTemplate.convertAndSend("/meeting/" + meetingId, response);
        }catch (Exception e){
            log.error("socket send error : {}", e);
            throw new RuntimeException("socket send error");
        }
    }
    
    @PostMapping(value = "/transcript", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> transcript(
            @RequestHeader String token,
            MultipartFile file,
            String meetingId) throws IOException {
        Map<String, String> resultMap = new HashMap<>();
        long userId = -1;
        String userName = "";
        try{
            Map<String, String> userData = getDataFromToken(token);
            userId = Long.parseLong(userData.get("userId"));
            userName = userData.get("userName");
            meetingId = decryptMeetingId(meetingId);
        }catch (Exception e){
            log.error("error : {}", e);
            resultMap.put("message", "error");
            return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
        }
            String filePath = saveAudio(meetingId, userId, file);// 얘네는 서버쪽에서 에러 발생함
            String stt = getStt(filePath); // 얘네는 서버쪽에서 에러 발생함


        HttpStatus status = null;



        try {
            String textResponse = openAIService.whisperAPICall(filePath).block();
            assert textResponse != null;

            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("type", "stt");
            responseMap.put("userId", "임시 유저 이름");
            responseMap.put("content", (String) gson.fromJson(textResponse, HashMap.class).get("text"));

            String response = gson.toJson(responseMap).toString();
            messagingTemplate.convertAndSend("/meeting/" + meetingId, response);
            resultMap.put("text", textResponse);
        } catch (Exception e) {
            log.error("텍스트 보내기 실패 : {}", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status.OK);
    }

    @PostMapping(value = "/message", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> getMessage(
            @RequestHeader("Authorization") String token,
            Long meetingId, String content) throws InterruptedException {

        token = token.split(" ")[1];
        String userName = "";
        try {
            Long userId = jwtService.extractUserId(token);
            userName = jwtService.extractUserName(token);
        } catch (Exception e) {
            log.error("아이디 뽑아내기 실패", e);
        }

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("type", "chat");
        responseMap.put("userId", userName.toString());
        responseMap.put("content", content);
        String response = gson.toJson(responseMap).toString();

        messagingTemplate.convertAndSend("/meeting/" + meetingId, response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/connect")
    public ResponseEntity<Map<String, String>> connectMeeting(@RequestHeader("Authorization") String token,
                                                              @RequestBody Map<String, Long> request) {
        HttpStatus status = HttpStatus.OK;
        Map<String, String> responseMessage = new HashMap<>();
        Long meetingId = request.get("meetingId");
        token = token.split(" ")[1];
        String userName = jwtService.extractUserName(token);

        responseMessage.put("message",  userName + "님께서" + meetingId  + "회의에 성공적으로 연결되었습니다.");
        try {
            // 회의 연결 로직
            // 예: 데이터베이스에 연결 정보를 저장하거나 필요한 작업 수행
        } catch (Exception e) {
            log.error("회의 연결 실패: {}", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            responseMessage.put("message", "회의에 연결하지 못했습니다.");
        }

        return new ResponseEntity<>(responseMessage, status);
    }


    // 사용자가 회의에서 연결을 끊을 때
    @PostMapping("/disconnect")
    public ResponseEntity<Map<String, String>> disconnectMeeting(@RequestHeader("Authorization") String token,
                                                                 @RequestBody Map<String, Long> request) {
        HttpStatus status = HttpStatus.OK;
        Map<String, String> responseMessage = new HashMap<>();
        Long meetingId = request.get("meetingId");
        token = token.split(" ")[1];
        String userName = jwtService.extractUserName(token);

        responseMessage.put("message",  userName + "님께서" + meetingId  + "회의에 성공적으로 연결되었습니다.");
        try {
            // 회의 연결 해제 로직
            // 예: 데이터베이스에서 연결 정보를 제거하거나 필요한 작업 수행
        } catch (Exception e) {
            log.error("회의 연결 끊기 실패: {}", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            responseMessage.put("message", "회의에 연결을 끊지 못했습니다.");
        }
        log.info("나간다 !!!!");
        return new ResponseEntity<>(responseMessage, status);
    }

    @PostMapping("/check-connection")
    public ResponseEntity<Map<String, String>> checkConnection(@RequestHeader("Authorization") String token,
                                                               @RequestBody Map<String, Long> request) {
        HttpStatus status = HttpStatus.OK;
        Map<String, String> responseMessage = new HashMap<>();
        Long meetingId = request.get("meetingId");
        token = token.split(" ")[1];
        String userName = jwtService.extractUserName(token);
        responseMessage.put("message",  userName + "님께서" + meetingId  + "회의에 성공적으로 연결 중입니다.");
        // 접속 확인 로직
        // 예: 데이터베이스에서 해당 회의 ID와 사용자의 접속 정보 확인
        log.info("연결 확인.");
        return new ResponseEntity<>(responseMessage, status);
    }
}
