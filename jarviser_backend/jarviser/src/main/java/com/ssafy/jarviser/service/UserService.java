package com.ssafy.jarviser.service;

import com.ssafy.jarviser.dto.*;
import com.ssafy.jarviser.domain.User;


public interface UserService {
    //로그인
    ResponseAuthenticationDto login(RequestLoginDto loginDto) throws Exception;
    //마이페이지
    public User mypage(long id) throws Exception;
    //회원가입
    public void regist(RequestUserDto dto) throws Exception;
    //회원탈퇴
    public void withdrawal(long id) throws Exception;
    //회원정보수정
    public ResponseUpdatedDto update(long id, RequestUpdateUserDto updateUserDto) throws Exception;
}
