package com.ssafy.jarviser.service;

import com.ssafy.jarviser.domain.RequestUserDto;
import com.ssafy.jarviser.domain.User;
import com.ssafy.jarviser.repository.UserRepository;
import com.ssafy.jarviser.security.JWTTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JWTTokenProvider tokenProvider;
    private final PasswordEncoder encoder;
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
        dto.setPassword(encoder.encode(dto.getPassword())); //security encode password
        User user = dto.toEntity();
        userRepository.save(user);
        log.info("DB에 회원 저장 성공");
    }
}
