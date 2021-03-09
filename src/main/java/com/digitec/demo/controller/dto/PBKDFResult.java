package com.digitec.demo.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PBKDFResult {
    private final byte[] salt;
    private final byte[] key;
}
