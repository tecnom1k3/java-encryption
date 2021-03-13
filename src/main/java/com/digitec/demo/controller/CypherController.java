package com.digitec.demo.controller;

import com.digitec.demo.contract.EncryptionServiceInterface;
import com.digitec.demo.contract.KeyServiceInterface;
import com.digitec.demo.dto.CipherNode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;

@RestController
@RequestMapping("/security")
public class CypherController {

    @Resource
    private EncryptionServiceInterface encryptionService;

    @Resource
    private KeyServiceInterface keyService;

    @PutMapping("/")
    public String encrypt(@RequestBody String input) {
        try {
            CipherNode cipherNode = encryptionService.symetricEncrypt(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(cipherNode.asBytes());
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }

        return null;
    }

    @PostMapping("/")
    public String decrypt(@RequestBody String input) {
        try {
            CipherNode cipherNode = CipherNode.fromBytes(Base64.getDecoder().decode(input));
            byte[] plaintext = encryptionService.symetricDecrypt(cipherNode.getCipherText(), cipherNode.getMac());
            return new String(plaintext, StandardCharsets.UTF_8);
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }

        return null;
    }

    @PostMapping("/key")
    public String generateKeys() throws GeneralSecurityException, IOException {
        keyService.generateKeys();
        return "ok";
    }

    @PutMapping("/key")
    public String rotateKeys() throws IOException, GeneralSecurityException {
        keyService.rotateKeys();
        return "ok";
    }

}
