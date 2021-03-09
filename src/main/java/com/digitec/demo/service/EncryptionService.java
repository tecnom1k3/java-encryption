package com.digitec.demo.service;

import com.digitec.demo.controller.dto.CipherParameters;
import com.digitec.demo.controller.dto.CipherResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import java.util.Arrays;

@Service
public class EncryptionService {

    private final Logger logger = LoggerFactory.getLogger(EncryptionService.class);
    @Resource
    CipherService cipherService;

    public CipherResult cipher(byte[] message) {

        try {
            CipherParameters cipherParameters = cipherService.initCipher(Cipher.ENCRYPT_MODE);
            Cipher cipher = cipherParameters.getCipherNode().getCipher();
            Mac mac = cipherParameters.getMacNode().getMac();

            byte[] cipherText = cipher.doFinal(message);

            return CipherResult.builder()
                    .iv(cipher.getIV())
                    .salt(cipherParameters.getCipherNode().getSalt())
                    .cipherText(cipherText)
                    .mac(mac.doFinal(cipherText))
                    .saltMac(cipherParameters.getMacNode().getSalt())
                    .build();
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] decipher(byte[] cypherText) {

        CipherResult cipherResult = CipherResult.fromBytes(cypherText);

        try {
            CipherParameters cipherParameters = cipherService.initDecipher(Cipher.DECRYPT_MODE, cipherResult.getIv(), cipherResult.getSalt(), cipherResult.getSaltMac());

            byte[] cipherTextMac = cipherParameters.getMacNode().getMac().doFinal(cipherResult.getCipherText());

            if (Arrays.equals(cipherTextMac, cipherResult.getMac())) {
                Cipher cipher = cipherParameters.getCipherNode().getCipher();
                return (cipher != null) ? cipher.doFinal(cipherResult.getCipherText()) : null;
            }

            throw new RuntimeException("Signature does not match");

        } catch (BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return null;
    }
}
