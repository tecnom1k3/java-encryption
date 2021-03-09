package com.digitec.demo.service;

import com.digitec.demo.controller.dto.PBKDFResult;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class PBKDF {
    public static PBKDFResult compute(String key, int keyLength) {

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        return getPbkdfResult(key, keyLength, salt);

    }

    private static PBKDFResult getPbkdfResult(String key, int keyLength, byte[] salt) {
        KeySpec spec = new PBEKeySpec(key.toCharArray(), salt, 65536, keyLength * 8);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            SecretKey secretKey = factory.generateSecret(spec);

            return PBKDFResult.builder()
                    .salt(salt)
                    .key(secretKey.getEncoded())
                    .build();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static PBKDFResult computeSalted(String key, int keyLength, byte[] salt) {

        return getPbkdfResult(key, keyLength, salt);

    }
}
