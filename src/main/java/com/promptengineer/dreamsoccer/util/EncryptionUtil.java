package com.promptengineer.dreamsoccer.util;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author pirda Pirdaus Ripa Atullah Gopur
Created on 08/02/2025 22:05
@Last Modified 08/02/2025 22:05
Version 1.0
*/
import java.util.Base64;

public class EncryptionUtil {
    public static String encrypt(String userId) {
        return Base64.getEncoder().encodeToString(userId.getBytes());
    }

    public static String decrypt(String encryptedUserId) {
        return new String(Base64.getDecoder().decode(encryptedUserId));
    }
}

