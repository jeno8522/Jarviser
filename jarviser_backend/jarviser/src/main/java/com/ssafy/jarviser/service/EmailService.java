package com.ssafy.jarviser.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendEmail(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("안녕하세요, 국가공인 맞짱플랫폼 MAZZANGGO?입니다.");
        message.setTo("dnddl9368@gmail.com");
        message.setText("나현웅님으로부터 맞짱신청이 도착하였습니다.\n도전에 응하시겠습니까?\nhttps://m.cafe.daum.net/subdued20club/_rec");

        javaMailSender.send(message);
    }
}