package com.digitec.demo.contract;

import com.digitec.demo.dto.CipherNode;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface EncryptionServiceInterface {

    CipherNode symetricEncrypt(byte[] input) throws IOException, GeneralSecurityException;

    byte[] symetricDecrypt(byte[] ciphertext, byte[] macTag) throws IOException, GeneralSecurityException;
}
