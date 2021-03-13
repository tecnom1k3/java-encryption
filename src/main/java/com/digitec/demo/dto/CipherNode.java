package com.digitec.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.nio.ByteBuffer;
import java.util.Arrays;

@Builder
@AllArgsConstructor
@Getter
public class CipherNode {
    private final byte[] cipherText;
    private final byte[] mac;

    public static CipherNode fromBytes(byte[] input) {
        byte[] mac = Arrays.copyOfRange(input, 0, 21);
        byte[] cipherText = Arrays.copyOfRange(input, 21, input.length);
        return CipherNode.builder().cipherText(cipherText).mac(mac).build();
    }

    public byte[] asBytes() {
        byte[] combinedBytes = new byte[cipherText.length + mac.length];
        ByteBuffer buffer = ByteBuffer.wrap(combinedBytes);
        buffer.put(mac);
        buffer.put(cipherText);
        return buffer.array();
    }
}
