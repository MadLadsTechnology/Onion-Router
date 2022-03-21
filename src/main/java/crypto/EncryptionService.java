package crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class EncryptionService {

    /**
     *
     * Decrypts a message with the RSA encryption algorithm
     *
     * @param encryptedMessage the message to be decrypted
     * @param privateKey the private key to decrypt the message
     * @return the decrypted string
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException if the cipher can't find the RSA algorithm
     * @throws InvalidKeyException if the give key isn't a valid RSA key
     * @throws IllegalBlockSizeException if the given message is too small or large for decryption
     * @throws BadPaddingException
     */
    public String rsaDecrypt(String encryptedMessage, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        byte[] bytes = Base64.getDecoder().decode(encryptedMessage);

        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        return Base64.getEncoder().encodeToString(decryptCipher.doFinal(bytes));
    }

    /**
     * Encrypts a string using RSA encryption algorithm
     *
     * @param string the string to be encrypted
     * @param publicKey the public key to be used for encrypting
     * @return The encrypted string
     * @throws Exception if the encryption could not be done
     */

    public String rsaEncrypt(byte[] string, PublicKey publicKey)throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] cipherText = encryptCipher.doFinal(string);

        return Base64.getEncoder().encodeToString(cipherText);
    }

    public Key keyFromString(String key, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(keySpec);
    }

}
