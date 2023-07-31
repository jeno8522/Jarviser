package com.ssafy.jarviser.service;

import com.ssafy.jarviser.dto.*;
import com.ssafy.jarviser.domain.User;
import com.ssafy.jarviser.repository.UserRepository;
import com.ssafy.jarviser.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    @Override
    // TODO: 2023-07-25
    public ResponseAuthenticationDto login(RequestLoginDto loginDto) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );
        var user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return ResponseAuthenticationDto.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public User mypage(long userid) throws Exception {
        return userRepository.findById(userid);
    }

    @Override
    public void regist(RequestUserDto dto) throws Exception {
        dto.setPassword(encoder.encode(dto.getPassword())); //security encode password
        User user = dto.toEntity();
        userRepository.save(user);
        log.info("DB에 회원 저장 성공");
    }

    @Override
    public void withdrawal(long id) throws Exception {
        userRepository.deleteById((int) id);
    }


    @Override
    public ResponseUpdatedDto update(long id, RequestUpdateUserDto updateUserDto) throws Exception {
        userRepository.updateUser(id,updateUserDto.getPassword(),updateUserDto.getName());

        return null;
    }
}
