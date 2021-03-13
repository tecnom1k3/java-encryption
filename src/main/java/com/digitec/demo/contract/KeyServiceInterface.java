package com.digitec.demo.contract;

import com.google.crypto.tink.KeysetHandle;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface KeyServiceInterface {
    void generateKeys() throws GeneralSecurityException, IOException;

    KeysetHandle loadKey() throws IOException, GeneralSecurityException;

    KeysetHandle loadMacKey() throws IOException, GeneralSecurityException;

    void rotateKeys() throws IOException, GeneralSecurityException;
}
