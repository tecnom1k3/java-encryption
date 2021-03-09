package com.digitec.demo.controller;

import com.digitec.demo.controller.dto.CipherResult;
import com.digitec.demo.service.EncryptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/security")
public class CypherController {

    private final Logger logger = LoggerFactory.getLogger(CypherController.class);
    @Resource
    private EncryptionService cypherService;

    @PutMapping("/")
    public String encrypt(@RequestBody String input) {
        CipherResult output = cypherService.cipher(input.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(output.asBytes());
    }

    @PostMapping("/")
    public String decrypt(@RequestBody String input) {
        byte[] cipherText = Base64.getDecoder().decode(input);
        byte[] output = cypherService.decipher(cipherText);
        return new String(output, StandardCharsets.UTF_8);
    }

}
