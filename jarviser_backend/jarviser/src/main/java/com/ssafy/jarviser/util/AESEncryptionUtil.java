package com.ssafy.jarviser.util;

import com.ssafy.jarviser.exception.ClientException;
import com.ssafy.jarviser.exception.ServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Base64;

@Component
@Slf4j
@RequiredArgsConstructor
public class AESEncryptionUtil {
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";

    @Value("${jarviserEncrypt.secretKey}")
    private String secretKey;

    static {
        // Bouncy Castle 프로바이더 등록
        Security.addProvider(new BouncyCastleProvider());
    }

    public String encrypt(String data){ // 인스턴스 메서드로 변경
        try {
            SecretKeySpec key = generateSecretKey();
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        }catch (Exception e){
            log.error("encrypt error : {}", e);
            throw new ServerException("encrypt error");
        }
    }
    public String decrypt(String encryptedData){ // 인스턴스 메서드로 변경
        try{
            SecretKeySpec key = generateSecretKey();
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        }catch (Exception e) {
            log.error("decrypt error : {}", e);
            throw new ClientException("decrypt error");
        }
    }

    private SecretKeySpec generateSecretKey() throws Exception {
        byte[] keyBytes = this.secretKey.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, AES_ALGORITHM);
    }
}
