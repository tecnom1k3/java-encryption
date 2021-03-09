package com.digitec.demo.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class CipherParameters {
    private final CipherNode cipherNode;
    private final MacNode macNode;
}
