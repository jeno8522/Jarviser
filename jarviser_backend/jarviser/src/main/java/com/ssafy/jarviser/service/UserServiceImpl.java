package com.ssafy.jarviser.service;

import com.ssafy.jarviser.domain.RequestUserDto;
import com.ssafy.jarviser.domain.User;
import com.ssafy.jarviser.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Override
    // TODO: 2023-07-25
    public User login(User user) throws Exception {
        return null;
    }

    @Override
    public User userInfo(String userid) throws Exception {
        return null;
    }

    @Override
    public void regist(RequestUserDto dto) throws Exception {
        // TODO: 2023-07-25 비밀번호 encrypt해서 저장할 것.

        User user = dto.toEntity();
        userRepository.save(user);
        log.info("DB에 회원 저장 성공");
    }
}
