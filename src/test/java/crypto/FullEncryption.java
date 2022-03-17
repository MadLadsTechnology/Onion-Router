package crypto;

import org.junit.Assert;
import org.junit.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class FullEncryption {

    @Test
    public void testRsaEncryptAndDecrypt() throws Exception {
        AESEncryption aesEncryption = new AESEncryption();

        SecretKey secretKey = aesEncryption.getAESKey();

        String message = "Hello there";

        String encryptedMessage = aesEncryption.encrypt(message, secretKey);

        RSAKeyPairGenerator keyPairGenerator = new RSAKeyPairGenerator();
        EncryptionService encryptionService = new EncryptionService();

        String encryptedAesKey = encryptionService.rsaEncrypt(secretKey.getEncoded(), keyPairGenerator.getPublicKey());

        String decryptedAesKey = encryptionService.rsaDecrypt(encryptedAesKey, keyPairGenerator.getPrivateKey());

        byte[] decodedKey = Base64.getDecoder().decode(decryptedAesKey);

        // rebuild key using SecretKeySpec
        SecretKey aesKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        String finalMessage = aesEncryption.decrypt(encryptedMessage, aesKey);

        Assert.assertEquals(secretKey, aesKey);
        Assert.assertEquals(message, finalMessage);
    }
}

