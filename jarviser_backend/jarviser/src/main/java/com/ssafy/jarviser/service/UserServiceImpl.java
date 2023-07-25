package com.ssafy.jarviser.service;

import com.ssafy.jarviser.domain.User;
import com.ssafy.jarviser.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
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
    public void regist(User user) throws Exception {
        // TODO: 2023-07-25 비밀번호 encrypt해서 저장할 것.
        userRepository.save(user);
    }

}
