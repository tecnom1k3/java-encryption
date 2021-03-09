package com.digitec.demo.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.crypto.SecretKey;

@Builder
@Getter
@AllArgsConstructor
public class SecretKeyParams {
    private final SecretKey secretKey;
    private final byte[] salt;
}
