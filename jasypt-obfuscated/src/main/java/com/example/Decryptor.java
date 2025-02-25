package com.example;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class Decryptor {
    public String decrypt(String password, String input) {
        StandardPBEStringEncryptor passwordDecryptor = new StandardPBEStringEncryptor();
        passwordDecryptor.setPassword(password);
        String decryptedInput = passwordDecryptor.decrypt(input);

        return decryptedInput;
    }
}
