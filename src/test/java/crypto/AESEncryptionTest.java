package crypto;

import org.junit.jupiter.api.Assertions;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

class AESEncryptionTest {


    @org.junit.jupiter.api.Test
    public void testEncryptDecrypt() throws NoSuchAlgorithmException{
        AESEncryption aesEncryption = new AESEncryption();

        SecretKey secretKey = aesEncryption.getAESKey();

        String message = "Darth Vader: Ah! Obi-Wan Kenobi: YOU WERE THE CHOSEN ONE! It was said that you would destroy the Sith, not join them, bring balance to the force, not leave it in darkness.";

        String encryptedMessage = aesEncryption.encrypt(message, secretKey);

        String decryptedMessage = aesEncryption.decrypt(encryptedMessage, secretKey);

        Assertions.assertNotEquals(message, encryptedMessage);
        Assertions.assertEquals(message, decryptedMessage);

    }

}
