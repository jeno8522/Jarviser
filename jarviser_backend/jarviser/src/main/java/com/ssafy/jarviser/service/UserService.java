package com.ssafy.jarviser.service;

import com.ssafy.jarviser.domain.RequestUserDto;
import com.ssafy.jarviser.domain.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


public interface UserService {
    public User login(User user) throws Exception;
    public User userInfo(String userid) throws Exception;
    public void regist(RequestUserDto dto) throws Exception;


}
