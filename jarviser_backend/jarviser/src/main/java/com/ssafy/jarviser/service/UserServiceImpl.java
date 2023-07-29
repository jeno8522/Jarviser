package com.ssafy.jarviser.service;

import com.ssafy.jarviser.dto.RequestLoginDto;
import com.ssafy.jarviser.dto.RequestUserDto;
import com.ssafy.jarviser.domain.User;
import com.ssafy.jarviser.dto.ResponseAuthenticationDto;
import com.ssafy.jarviser.repository.UserRepository;
import com.ssafy.jarviser.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
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
    public User mypage(String userid) throws Exception {
        return null;
    }

    @Override
    public void regist(RequestUserDto dto) throws Exception {
        dto.setPassword(encoder.encode(dto.getPassword())); //security encode password
        User user = dto.toEntity();
        userRepository.save(user);
        log.info("DB에 회원 저장 성공");
    }

    @Override
    public long loggout(String userid) throws Exception {
        return 0;
    }

    @Override
    public void withdrawal(String userid) throws Exception {

    }

    @Override
    public long update(String userid) throws Exception {
        return 0;
    }

}
