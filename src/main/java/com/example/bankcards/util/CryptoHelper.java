package com.example.bankcards.util;

import com.example.bankcards.exception.CryptoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Component
public class CryptoHelper {

    private static final String ALGORITHM = "AES";
    private final SecretKey secretKey;

    public CryptoHelper(@Value("${crypto.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ALGORITHM);
    }

    public byte[] encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new CryptoException("Failed to encrypt data", e);
        }
    }

    public String decrypt(byte[] encrypted) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new CryptoException("Failed to decrypt data", e);
        }
    }
}

