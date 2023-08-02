package com.ssafy.jarviser.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootTest
class EmailServiceTest {
    @Autowired
    EmailService emailService;

    @Test
    @DisplayName("회원 가입 테스팅")
    void testEmailSend(){
       emailService.sendEmail();
    }
}