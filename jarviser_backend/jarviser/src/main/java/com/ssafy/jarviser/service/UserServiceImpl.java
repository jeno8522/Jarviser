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
        var user = userRepository.findByEmail(loginDto.getEmail());

        var jwtToken = jwtService.generateToken(user);
        return ResponseAuthenticationDto.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public ResponseMypageDto mypage(long userid) throws Exception {
        User user = userRepository.getReferenceById(userid);
        return new ResponseMypageDto(user.getEmail(), user.getName());
    }

    @Override
    public void regist(RequestUserDto dto) throws Exception {
        dto.setPassword(encoder.encode(dto.getPassword())); //security encode password
        User user = dto.toEntity();
        userRepository.save(user);
        log.info("DB에 회원 저장 성공");
    }

    @Override
    public void withdrawal(Long id) throws Exception {
        userRepository.deleteById(id);
    }


    @Override
    public void update(long id, RequestUpdateUserDto updateUserDto) throws Exception {
        userRepository.updateUserById(id,updateUserDto.getPassword(),updateUserDto.getName());
    }

    @Override
    public User findUserById(Long id) throws Exception {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        return userRepository.findByEmail(email);
    }
}