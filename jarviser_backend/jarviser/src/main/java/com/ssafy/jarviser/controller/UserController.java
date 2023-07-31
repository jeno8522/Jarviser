package com.ssafy.jarviser.controller;

import com.ssafy.jarviser.domain.User;
import com.ssafy.jarviser.dto.*;
import com.ssafy.jarviser.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signUp(@RequestBody RequestUserDto requestUserDto) {
        log.debug("User............................regist user:" + requestUserDto);

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        try {
            userService.regist(requestUserDto);
            resultMap.put("message", SUCCESS);
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            //FIXME : 모든 회원 가입 실패에 대하여 처리가 필요함
            log.error("회원가입 실패 : {}", e);
            resultMap.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody RequestLoginDto requestLoginDto){
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
            resultMap.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @PatchMapping("/{userid}")
    public ResponseEntity<Map<String,Object>> update(@PathVariable long userid, @RequestBody RequestUpdateUserDto requestUpdateUserDto){
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        try{
            userService.update(userid,requestUpdateUserDto);
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/{userid}")
    public ResponseEntity<Map<String,Object>> mypage(@PathVariable("userid") long id){
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        try{
            ResponseMypageDto responseMypageDto = userService.mypage(id);
            resultMap.put("response",responseMypageDto);
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @DeleteMapping("/{userid}")
    public ResponseEntity<Map<String,Object>> delete(@PathVariable long userid){
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        try{
            userService.withdrawal(userid);
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(resultMap, status);
    }
}