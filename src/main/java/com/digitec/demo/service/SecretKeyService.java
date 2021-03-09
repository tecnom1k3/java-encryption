package com.digitec.demo.service;

import com.digitec.demo.controller.dto.PBKDFResult;
import com.digitec.demo.controller.dto.SecretKeyParams;

import javax.crypto.spec.SecretKeySpec;


public class SecretKeyService {

    private static final String secretKey = "DCgvzaM8Ozkz52DoxD2Xqce4ewIex4zM";

    public static SecretKeyParams getSecretKey() throws RuntimeException {

        PBKDFResult pbkdfResult = PBKDF.compute(secretKey, 32);

        if (pbkdfResult != null) {
            return SecretKeyParams.builder()
                    .salt(pbkdfResult.getSalt())
                    .secretKey(new SecretKeySpec(pbkdfResult.getKey(), "AES"))
                    .build();
        }

        throw new RuntimeException("unable to generate key");

    }

    public static SecretKeyParams getSaltedSecretKey(byte[] salt) throws RuntimeException {

        PBKDFResult pbkdfResult = PBKDF.computeSalted(secretKey, 32, salt);

        if (pbkdfResult != null) {
            return SecretKeyParams.builder()
                    .salt(pbkdfResult.getSalt())
                    .secretKey(new SecretKeySpec(pbkdfResult.getKey(), "AES"))
                    .build();
        }

        throw new RuntimeException("unable to generate key");

    }

}
