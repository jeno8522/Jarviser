package com.ssafy.jarviser.security;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(locations = "classpath:jwt.yml")
public class JWTTokenProviderTest {

    private JWTTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setup() {
        jwtTokenProvider = new JWTTokenProvider("eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJBNTA2IiwiVXNlcm5hbWUiOiLrrLjtmY3sm4XrsJTrs7QiLCJleHAiOjE2OTAzMzU2NzIsImlhdCI6MTY5MDMzNTY3Mn0.JpG28FCZgjuuENwEtlu4Id0v-zHtOkicM9onfEXWjAM", 1L/1800, "issuer");
    }

    @Test
    public void createToken_Success() {
        String userSpecification = "TestUser";
        String token = jwtTokenProvider.createToken(userSpecification);

        assertNotNull(token);
        assertEquals(userSpecification, jwtTokenProvider.validateTokenAndGetSubject(token));
    }

    @Test
    public void validateTokenAndGetSubject_ExpiredToken_ExceptionThrown() throws InterruptedException {
        String userSpecification = "TestUser";
        String token = jwtTokenProvider.createToken(userSpecification);

        Thread.sleep(1000 * 3); // Sleep for two seconds to ensure the token has expired

        assertThrows(ExpiredJwtException.class, () -> {
            jwtTokenProvider.validateTokenAndGetSubject(token);
        });
    }
}
