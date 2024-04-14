import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.security.PrivateKey;
import java.security.KeyStore;
import javax.crypto.Cipher;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.KeyFactory;

public class Decryptor {
    public static void main(String[] args) throws Exception {
        // DER形式の秘密鍵の読み込みテスト
        {
            byte[] keyBytes = Files.readAllBytes(Paths.get("04_key.der"));

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            Cipher decryptor = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            decryptor.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] encrypted = Files.readAllBytes(Paths.get("encrypted.bin"));

            String decrypted = new String(decryptor.doFinal(encrypted), StandardCharsets.UTF_8);

            System.out.println("decrypted message = " + decrypted);
        }

        // cert.jksの読み込みテスト
        {

            KeyStore keyStore = KeyStore.getInstance("JKS");

            char[] password = "Password3".toCharArray();
            keyStore.load(new FileInputStream(new File("03_cert.jks")), password);

            PrivateKey privateKey = (PrivateKey)keyStore.getKey("certificate", password);

            Cipher decryptor = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            decryptor.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] encrypted = Files.readAllBytes(Paths.get("encrypted.bin"));

            String decrypted = new String(decryptor.doFinal(encrypted), StandardCharsets.UTF_8);

            System.out.println("decrypted message = " + decrypted);
        }
    }
}
