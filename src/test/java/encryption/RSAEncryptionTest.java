package encryption;

import org.junit.jupiter.api.Assertions;

import java.util.Base64;

class RSAEncryptionTest {
    @org.junit.jupiter.api.Test
    public void testRsaEncryptAndDecrypt() throws Exception {

        String message= "test";
        RSAEncryption encryption = new RSAEncryption();

        String encrypted = encryption.rsaEncrypt(Base64.getDecoder().decode(message), encryption.getPublicKey());

        Assertions.assertEquals( message, encryption.rsaDecrypt(encrypted, encryption.getPrivateKey()));
    }
}