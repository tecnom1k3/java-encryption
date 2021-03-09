package com.digitec.demo.service;

import com.digitec.demo.controller.dto.CipherNode;
import com.digitec.demo.controller.dto.CipherParameters;
import com.digitec.demo.controller.dto.MacNode;
import com.digitec.demo.controller.dto.SecretKeyParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


@Service
public class CipherService {

    private final Logger logger = LoggerFactory.getLogger(CipherService.class);

    public CipherParameters initCipher(int cipherMode) {
        try {
            Cipher cipher = getCipherInstance();
            SecretKeyParams secretKeyParams = SecretKeyService.getSecretKey();
            SecretKey key = secretKeyParams.getSecretKey();
            byte[] salt = secretKeyParams.getSalt();
            IvParameterSpec ivParameterSpec = IvService.getRandomIvParameterSpec();
            cipher.init(cipherMode, key, ivParameterSpec);

            SecretKeyParams secretKeyParamsMac = SecretKeyService.getSecretKey();
            Mac mac = getMac(secretKeyParamsMac);

            CipherNode cipherNode = CipherNode.builder()
                    .cipher(cipher)
                    .salt(salt)
                    .build();

            MacNode macNode = MacNode.builder()
                    .mac(mac)
                    .salt(secretKeyParamsMac.getSalt())
                    .build();

            return CipherParameters.builder()
                    .cipherNode(cipherNode)
                    .macNode(macNode)
                    .build();
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        return null;
    }

    public CipherParameters initDecipher(int cipherMode, byte[] ivBytes, byte[] salt, byte[] saltMac) {

        try {
            Cipher cipher = getCipherInstance();
            SecretKeyParams secretKeyParams = SecretKeyService.getSaltedSecretKey(salt);
            SecretKey key = secretKeyParams.getSecretKey();
            IvParameterSpec ivParameterSpec = IvService.getIvParameterSpec(ivBytes);
            cipher.init(cipherMode, key, ivParameterSpec);

            SecretKeyParams secretKeyParamsMac = SecretKeyService.getSaltedSecretKey(saltMac);
            Mac mac = getMac(secretKeyParamsMac);

            CipherNode cipherNode = CipherNode.builder()
                    .cipher(cipher)
                    .salt(salt)
                    .build();

            MacNode macNode = MacNode.builder()
                    .mac(mac)
                    .salt(saltMac)
                    .build();

            return CipherParameters.builder()
                    .cipherNode(cipherNode)
                    .macNode(macNode)
                    .build();
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Mac getMac(SecretKeyParams secretKeyParamsMac) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKey secretKeyMac = secretKeyParamsMac.getSecretKey();
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeyMac);
        return mac;
    }

    private Cipher getCipherInstance() throws NoSuchAlgorithmException, NoSuchPaddingException {
        return Cipher.getInstance("AES/CBC/PKCS5Padding");
    }
}
