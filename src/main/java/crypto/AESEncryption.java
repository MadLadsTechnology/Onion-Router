package crypto;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Class to handle AES encryption:
 *
 * Key generating
 * Encryption and decryption
 */

public class AESEncryption {


    /**
     * Generates a random AES key
     *
     * @return the generated key
     * @throws NoSuchAlgorithmException
     */
    public SecretKey getAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256, SecureRandom.getInstanceStrong());
        return keyGen.generateKey();
    }



    /**
     * Encrypts a string using AES encryption
     * @param string The string
     * @param key the key to encrypt the string with
     * @return the encrypted string
     */
    public String encrypt(final String string, final SecretKey key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(string.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e);
        }
        return null;
    }

    /**
     * Decrypts an AES encrypted string
     *
     * @param string  the encrypted string to be decrypted
     * @param key the AES key used to decrypt
     * @return the decrypted string
     */
    public String decrypt(final String string, final SecretKey key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder()
                    .decode(string)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e);
        }
        return null;
    }


}
