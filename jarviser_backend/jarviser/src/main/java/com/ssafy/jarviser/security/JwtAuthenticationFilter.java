package com.ssafy.jarviser.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {//Request -> filter에서 jwt를 검증하도록

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain //거쳐갈 다른 필터들
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization"); //헤더에 jwt토큰
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); //검증되면 다음 필터로 넘거간다.
            return;
        }
        jwt = authHeader.substring(7); //authHeader에서 Bearer 이후의 실질적 jwt토큰을 뽑아냄.
        userEmail = jwtService.extractUserEmail(jwt);
    }

}
