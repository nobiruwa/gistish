package com.example;

import java.nio.charset.StandardCharsets;

/**
 * Hello world!
 */
public class App {
    private static final byte[] APP_PASSWORD_BYTES;
    private static final byte[] SYSTEM_PASSWORD_BYTES;

    static {
        // APP_PASSWORD_BYTES = "ThisIsAppSecret".getBytes();
        APP_PASSWORD_BYTES = new byte [] {
            84,
            104,
            105,
            115,
            73,
            115,
            65,
            112,
            112,
            83,
            101,
            99,
            114,
            101,
            116
        };
        // SYSTEM_PASSWORD_BYTES = "ThisIsSystemSecret".getBytes();
        SYSTEM_PASSWORD_BYTES = new byte [] {
            84,
            104,
            105,
            115,
            73,
            115,
            83,
            121,
            115,
            116,
            101,
            109,
            83,
            101,
            99,
            114,
            101,
            116
        };
    }
    public static void main(String[] args) {

        Encryptor encryptor = new Encryptor();
        Decryptor decryptor = new Decryptor();

        String input = args[0];
        System.out.println(input);

        String encrypted = encryptor.encrypt(new String(SYSTEM_PASSWORD_BYTES, StandardCharsets.UTF_8), input);

        String moreEncrypted = encryptor.encrypt(new String(APP_PASSWORD_BYTES, StandardCharsets.UTF_8), encrypted);

        System.out.println(encrypted);
        System.out.println(moreEncrypted);

        String decrypted = decryptor.decrypt(new String(APP_PASSWORD_BYTES, StandardCharsets.UTF_8), moreEncrypted);

        String moreDecrypted = decryptor.decrypt(new String(SYSTEM_PASSWORD_BYTES, StandardCharsets.UTF_8), decrypted);

        System.out.println(decrypted);
        System.out.println(moreDecrypted);
    }
}
