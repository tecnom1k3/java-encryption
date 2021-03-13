package com.digitec.demo.service;

import com.digitec.demo.contract.EncryptionServiceInterface;
import com.digitec.demo.contract.KeyServiceInterface;
import com.digitec.demo.dto.CipherNode;
import com.google.crypto.tink.Aead;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.Mac;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.mac.MacConfig;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

@Service
public class EncryptionService implements EncryptionServiceInterface {

    private final String ad = "mmKxoc6TPFln7pSi99ABrFXM3XiCVuozY0UNd3A69T8i4mGe9g6jLO6NkTaje3K";

    @Resource
    private KeyServiceInterface keyService;

    @PostConstruct
    private void init() throws GeneralSecurityException, IOException {
        AeadConfig.register();
        MacConfig.register();
    }

    @Override
    public CipherNode symetricEncrypt(byte[] input) throws IOException, GeneralSecurityException {
        Aead aead = getAead();
        Mac mac = getMac();
        byte[] cipherText = aead.encrypt(input, ad.getBytes(StandardCharsets.UTF_8));
        byte[] tag = mac.computeMac(cipherText);
        return CipherNode.builder().cipherText(cipherText).mac(tag).build();
    }

    private Mac getMac() throws IOException, GeneralSecurityException {
        KeysetHandle macKeysetHandle = keyService.loadMacKey();
        return macKeysetHandle.getPrimitive(Mac.class);
    }

    private Aead getAead() throws IOException, GeneralSecurityException {
        KeysetHandle keysetHandle = keyService.loadKey();
        return keysetHandle.getPrimitive(Aead.class);
    }

    @Override
    public byte[] symetricDecrypt(byte[] ciphertext, byte[] macTag) throws IOException, GeneralSecurityException {
        Aead aead = getAead();
        Mac mac = getMac();
        mac.verifyMac(macTag, ciphertext);
        return aead.decrypt(ciphertext, ad.getBytes(StandardCharsets.UTF_8));
    }

}
