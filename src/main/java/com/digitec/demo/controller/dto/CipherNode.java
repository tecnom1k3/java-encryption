package com.digitec.demo.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.crypto.Cipher;

@Getter
@AllArgsConstructor
@Builder
public class CipherNode {
    private final Cipher cipher;
    private final byte[] salt;
}
