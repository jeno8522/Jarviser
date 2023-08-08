package com.ssafy.jarviser.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Encrypto {

    @Test
    @DisplayName("EncryptoTest")
    void testEncrypt() throws Exception {
        long id = 1;
        String encrypt = AESEncryptionUtil.encrypt(Long.toString(id));
        System.out.println("?!?!?!?!?");
        System.out.println(encrypt);
    }
}
