package encryption;

import org.junit.Assert;
import org.junit.Test;

public class RSAEncryptionTest {
    @Test
    public void testRsaEncryptAndDecrypt() throws Exception {
        String message = "Hello World";
        RSAEncryption rsaEncryption = new RSAEncryption();

        String encrypted = rsaEncryption.rsaEncrypt(message.getBytes(), rsaEncryption.getPublicKey());

        String decrypted = rsaEncryption.rsaDecrypt(encrypted, rsaEncryption.getPrivateKey());

        Assert.assertEquals(decrypted, message);
    }
}