package com.ssafy.jarviser.service;

import com.ssafy.jarviser.dto.RequestLoginDto;
import com.ssafy.jarviser.dto.RequestUserDto;
import com.ssafy.jarviser.domain.User;
import com.ssafy.jarviser.dto.ResponseAuthenticationDto;


public interface UserService {

    ResponseAuthenticationDto login(RequestLoginDto loginDto) throws Exception;
    public User userInfo(String userid) throws Exception;
    public void regist(RequestUserDto dto) throws Exception;
}
