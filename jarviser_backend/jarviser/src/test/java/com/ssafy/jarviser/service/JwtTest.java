package com.ssafy.jarviser.service;

import com.ssafy.jarviser.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Slf4j
public class JwtTest {

    @Autowired
    JwtService jwtService;

    private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoic3RyaW5nIiwic3ViIjoic3RyaW5nIiwiaWF0IjoxNjkwOTU0NzI0LCJleHAiOjE2OTA5NTQ3MjR9.10bkZm7Rr3-i6QiuJ-J6YTTK380vMQCtpKzK7fPqGBo";

        @Test
        public void test() {
            String name = jwtService.extractUserName(token);
            Long id = jwtService.extractUserId(token);

            assertNotNull(name);
            assertNotNull(id);

            log.info("name : " + name);
            log.info("id : " + id);
        }
}
