package com.digitec.demo.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.nio.ByteBuffer;
import java.util.Arrays;

@Getter
@Builder
@AllArgsConstructor
public class CipherResult {
    private final byte[] iv;
    private final byte[] cipherText;
    private final byte[] salt;
    private final byte[] mac;
    private final byte[] saltMac;

    public static CipherResult fromBytes(byte[] input) {

        byte[] iv = Arrays.copyOfRange(input, 0, 16);
        byte[] salt = Arrays.copyOfRange(input, 16, 32);
        byte[] saltMac = Arrays.copyOfRange(input, 32, 48);
        byte[] mac = Arrays.copyOfRange(input, 48, 80);
        byte[] cipherText = Arrays.copyOfRange(input, 80, input.length);
        return CipherResult.builder()
                .iv(iv)
                .salt(salt)
                .saltMac(saltMac)
                .cipherText(cipherText)
                .mac(mac)
                .build();
    }

    public byte[] asBytes() {
        byte[] combinedBytes = new byte[iv.length + salt.length + cipherText.length + saltMac.length + mac.length];
        ByteBuffer buffer = ByteBuffer.wrap(combinedBytes);
        buffer.put(iv);
        buffer.put(salt);
        buffer.put(saltMac);
        buffer.put(mac);
        buffer.put(cipherText);
        return buffer.array();
    }
}
