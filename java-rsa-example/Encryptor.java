import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import javax.crypto.Cipher;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.security.spec.X509EncodedKeySpec;
import java.security.KeyFactory;
import java.security.PublicKey;

public class Encryptor {
    public static void main(String[] args) throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get("06_public.der"));

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        Cipher encryptor = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        encryptor.init(Cipher.ENCRYPT_MODE, publicKey);

        String plainMessage = "私はアシモフです。";

        byte[] encrypted = encryptor.doFinal(plainMessage.getBytes());

        Files.write(Paths.get("encrypted.bin"), encrypted);
    }
}
