package crypto;

import org.junit.Assert;
import org.junit.Test;

public class EncryptionServiceTest {
    @Test
    public void testRsaEncryptAndDecrypt() throws Exception {
        String message = "Hello World";
        RSAKeyPairGenerator keyPairGenerator = new RSAKeyPairGenerator();
        EncryptionService encryptionService = new EncryptionService();

        String encrypted = encryptionService.rsaEncrypt(message.getBytes(), keyPairGenerator.getPublicKey());

        String decrypted = encryptionService.rsaDecrypt(encrypted, keyPairGenerator.getPrivateKey());

        Assert.assertEquals(decrypted, message);
    }
}