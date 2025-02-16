package com.promptengineer.dreamsoccer.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtil {
    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "mysecretkey12345";

    private static SecretKey getSecretKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
    }

    public static String encrypt(String userId) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
            byte[] encryptedBytes = cipher.doFinal(userId.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error saat encrypting: " + e.toString());
        }
    }

    public static String decrypt(String encryptedUserId) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey());
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedUserId));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error saat decrypting: " + e.toString());
        }
    }
}
