package com.example;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class Encryptor {
    public String encrypt(String password, String input) {
        StandardPBEStringEncryptor passwordEncryptor = new StandardPBEStringEncryptor();
        passwordEncryptor.setPassword(password);
        String encryptedInput = passwordEncryptor.encrypt(input);

        return encryptedInput;
    }
}
