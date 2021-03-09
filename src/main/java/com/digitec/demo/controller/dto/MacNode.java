package com.digitec.demo.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.crypto.Mac;

@Getter
@AllArgsConstructor
@Builder
public class MacNode {
    private final Mac mac;
    private final byte[] salt;
}
