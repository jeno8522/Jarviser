package com.ssafy.jarviser.service;

import com.ssafy.jarviser.dto.RequestUserDto;
import com.ssafy.jarviser.domain.User;


public interface UserService {
    public User login(User user) throws Exception;
    public User userInfo(String userid) throws Exception;
    public void regist(RequestUserDto dto) throws Exception;
}
