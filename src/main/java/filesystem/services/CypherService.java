package filesystem.services;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Service
public class CypherService {

    private final SecretKey secretKey;
    private final byte[] initializationVector;
    private static final String AES = "AES";
    private static final int KEY_SIZE = 256;
    private static final String AES_CIPHER_ALGORITHM = "AES/CBC/PKCS5PADDING";

    public CypherService() throws NoSuchAlgorithmException {
        this.secretKey = createKey();
        this.initializationVector = createInitializationVector();
    }

    public void encryptFile(String file) throws Exception {
        Path filePath = Path.of(file);
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initializationVector);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

        byte[] encryptContent = cipher.doFinal(Files.readAllBytes(filePath));
        Files.write(filePath, encryptContent);
    }

    public void decryptFile(String file) throws Exception {
        Path filePath = Path.of(file);
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initializationVector);

        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

        byte[] decryptContent = cipher.doFinal(Files.readAllBytes(filePath));
        Files.write(filePath, decryptContent);
    }

    private static SecretKey createKey() throws NoSuchAlgorithmException {
        KeyGenerator keygenerator = KeyGenerator.getInstance(AES);
        keygenerator.init(KEY_SIZE, new SecureRandom());
        return keygenerator.generateKey();
    }

    private static byte[] createInitializationVector() {
        byte[] initializationVector
                = new byte[16];
        SecureRandom secureRandom
                = new SecureRandom();
        secureRandom.nextBytes(initializationVector);
        return initializationVector;
    }
}
