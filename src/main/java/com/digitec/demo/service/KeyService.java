package com.digitec.demo.service;

import com.digitec.demo.contract.KeyServiceInterface;
import com.google.crypto.tink.*;
import com.google.crypto.tink.aead.AesGcmKeyManager;
import com.google.crypto.tink.mac.HmacKeyManager;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
public class KeyService implements KeyServiceInterface {

    private final String keysetFileName = "my_keyset.json";
    private final String macKeysetFileName = "my_mac_keyset.json";

    @Override
    public void generateKeys() throws GeneralSecurityException, IOException {
        KeyTemplate keyTemplate = AesGcmKeyManager.aes256GcmTemplate();
        KeysetHandle keysetHandle = KeysetHandle.generateNew(keyTemplate);
        CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withFile(new File(keysetFileName)));


        KeysetHandle macKeysetHandle = KeysetHandle.generateNew(HmacKeyManager.hmacSha256HalfDigestTemplate());
        CleartextKeysetHandle.write(macKeysetHandle, JsonKeysetWriter.withFile(new File(macKeysetFileName)));
    }

    @Override
    public KeysetHandle loadKey() throws IOException, GeneralSecurityException {
        return CleartextKeysetHandle.read(JsonKeysetReader.withFile(new File(keysetFileName)));
    }

    @Override
    public KeysetHandle loadMacKey() throws IOException, GeneralSecurityException {
        return CleartextKeysetHandle.read(JsonKeysetReader.withFile(new File(macKeysetFileName)));
    }

    @Override
    public void rotateKeys() throws IOException, GeneralSecurityException {
        KeysetHandle keysetHandle = loadKey();
        KeyTemplate keyTemplate = AesGcmKeyManager.aes256GcmTemplate();

        KeysetHandle rotatedKeySetHandle = KeysetManager.withKeysetHandle(keysetHandle).add(keyTemplate).getKeysetHandle();
        CleartextKeysetHandle.write(rotatedKeySetHandle, JsonKeysetWriter.withFile(new File(keysetFileName)));
    }
}
