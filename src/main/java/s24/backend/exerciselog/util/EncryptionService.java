package s24.backend.exerciselog.util;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EncryptionService {
    
    private final SecretKeySpec secretKey;
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    public EncryptionService(@Value("${ENCRYPTION_SECRET}") String secret) throws NoSuchAlgorithmException {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = sha.digest(secret.getBytes(StandardCharsets.UTF_8));
            this.secretKey = new SecretKeySpec(keyBytes, "AES");
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException("Error initializing EncryptionService ", e);
        }
    }

    public String encrypt(String strToEncrypt) throws GeneralSecurityException {
        if (strToEncrypt == null) return null;

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // Generate IV
            byte[] iv = new byte[16]; // 16 bytes for AES
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            byte[] encryptedBytes = cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8));

            // Concatenate IV and encrypted bytes
            byte[] combined = new byte[iv.length + encryptedBytes.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);

            // Encode to Base64
            return Base64.getEncoder().encodeToString(combined);
        } catch (GeneralSecurityException e) {
            throw new GeneralSecurityException("Error encrypting data", e);
        }
    }

    public String decrypt(String strToDecrypt) throws GeneralSecurityException {
        if (strToDecrypt == null) return null;

        try {
            byte[] combined = Base64.getDecoder().decode(strToDecrypt);

            // Extract IV and encrypted bytes
            byte[] iv = Arrays.copyOfRange(combined, 0, 16);
            byte[] encryptedBytes = Arrays.copyOfRange(combined, 16, combined.length);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) { // Replace with GeneralSecurityException later TODO
            return strToDecrypt;
            //throw new GeneralSecurityException("Error decrypting data", e);
        }
    }
}
