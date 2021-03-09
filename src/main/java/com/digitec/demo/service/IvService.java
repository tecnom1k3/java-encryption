package com.digitec.demo.service;

import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class IvService {

    public static IvParameterSpec getRandomIvParameterSpec() throws NoSuchAlgorithmException {
        byte[] ivBytes = new byte[16];
        SecureRandom secureRandom = SecureRandom.getInstanceStrong();
        secureRandom.nextBytes(ivBytes);
        return getIvParameterSpec(ivBytes);
    }

    public static IvParameterSpec getIvParameterSpec(byte[] ivBytes) {
        return new IvParameterSpec(ivBytes);
    }
}
