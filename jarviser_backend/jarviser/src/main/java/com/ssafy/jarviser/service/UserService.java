package com.ssafy.jarviser.service;

import com.ssafy.jarviser.dto.RequestLoginDto;
import com.ssafy.jarviser.dto.RequestUserDto;
import com.ssafy.jarviser.domain.User;
import com.ssafy.jarviser.dto.ResponseAuthenticationDto;


public interface UserService {
    //로그인
    ResponseAuthenticationDto login(RequestLoginDto loginDto) throws Exception;
    //마이페이지
    public User mypage(String userid) throws Exception;
    //회원가입
    public void regist(RequestUserDto dto) throws Exception;
    //로그아웃
    public long loggout(String userid)  throws  Exception;
    //회원탈퇴
    public void withdrawal(String userid) throws Exception;
    //회원정보수정
    public long update(String userid) throws Exception;
}
