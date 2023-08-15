package com.ssafy.jarviser.controller;

import com.ssafy.jarviser.domain.AudioMessage;
import com.ssafy.jarviser.domain.Meeting;
import com.ssafy.jarviser.domain.User;
import com.ssafy.jarviser.dto.*;
import com.ssafy.jarviser.security.JwtService;
import com.ssafy.jarviser.service.MeetingService;
import com.ssafy.jarviser.service.UserService;
import com.ssafy.jarviser.util.AESEncryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.naming.ldap.PagedResultsControl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final MeetingService meetingService;
    private final AESEncryptionUtil aesEncryptionUtil;
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";


    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signUp(@RequestBody RequestUserDto requestUserDto) {
        log.debug("User............................regist user:" + requestUserDto);

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        try {
            userService.regist(requestUserDto);
            resultMap.put("message", SUCCESS);
            status = HttpStatus.ACCEPTED;
            //TODO: ExceptionHandler
        } catch (Exception e) {
            //FIXME : 모든 회원 가입 실패에 대하여 처리가 필요함
            log.error("회원가입 실패 : {}", e);
            resultMap.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody RequestLoginDto requestLoginDto) {
        log.debug("User............................regist user:" + requestLoginDto);

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        try {
            resultMap.put("message", SUCCESS);
            resultMap.put("access-token", userService.login(requestLoginDto).getToken());
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            //FIXME : 모든 회원 가입 실패에 대하여 처리가 필요함
            log.error("로그인 실패", e);
            resultMap.put("message", "아이디 또는 비밀번호를 다시 입력해주세요.");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    //회원정보수정
    @PatchMapping("/update")
    public ResponseEntity<Map<String, Object>> update(
            @RequestHeader("Authorization") String token,
            @RequestBody RequestUpdateUserDto requestUpdateUserDto) {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        token = token.split(" ")[1];
        try {
            Long userId = jwtService.extractUserId(token);
            userService.updateUser(userId, requestUpdateUserDto);
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> checkId(@PathVariable String email){
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        try {
            User user = userService.findUserByEmail(email);
            if (user == null){
                resultMap.put("message", SUCCESS);
            }else{
                resultMap.put("message", "중복된 이메일 입니다.");
            }
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            //FIXME : 모든 회원 가입 실패에 대하여 처리가 필요함
            log.error("아이디 찾기 오류", e);
            resultMap.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }
    //마이페이지
    @GetMapping("/mypage")
    public ResponseEntity<Map<String, Object>> mypage(
            @RequestHeader("Authorization") String token) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        token = token.split(" ")[1];
        try {
            Long userId = jwtService.extractUserId(token);
            ResponseMypageDto responseMypageDto = userService.mypage(userId);
            resultMap.put("response", responseMypageDto);
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(resultMap, status);
    }

    //회원탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> delete(
            @RequestHeader("Authorization") String token
    ) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        token = token.split(" ")[1];
        Long userid = jwtService.extractUserId(token);
        try {
            userService.withdrawal(userid);
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(resultMap, status);
    }

    //유저 참여 미팅 내역
    @GetMapping("/meetinglist")
    public ResponseEntity<Map<String, Object>> meetingList(
            @RequestHeader("Authorization") String token
    ) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        try {
            token = token.split(" ")[1];
            Long userid = jwtService.extractUserId(token);
            List<Meeting> meetingList = meetingService.findMeetingListByUserId(userid);
            List<ResponseMeetingDto> responseMeetingDtos = new ArrayList<>();

            for (Meeting meeting : meetingList) {
                User host = userService.findUserById(meeting.getHostId());
                responseMeetingDtos.add(new ResponseMeetingDto(meeting.getMeetingName(), host.getName(), meeting.getStartTime(), aesEncryptionUtil.encrypt(String.valueOf(meeting.getId()))));
            }
            resultMap.put("meetinglist", responseMeetingDtos);
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(resultMap, status);
    }


}