package crypto;

import nodeManager.NodeHandler;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;

public class AESEncryptionTest {


    @Test
    public void testEncryptDecrypt() throws NoSuchAlgorithmException {
        AESEncryption aesEncryption = new AESEncryption();

        SecretKey secretKey = aesEncryption.getAESKey();

        String message = "Darth Vader: Ah! Obi-Wan Kenobi: YOU WERE THE CHOSEN ONE! It was said that you would destroy the Sith, not join them, bring balance to the force, not leave it in darkness.";

        String encryptedMessage = aesEncryption.encrypt(message, secretKey);

        String decryptedMessage = aesEncryption.decrypt(encryptedMessage, secretKey);

        assert(!message.equals(encryptedMessage));
        assert(message.equals(decryptedMessage));

    }

}
