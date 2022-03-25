package encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAEncryption {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public RSAEncryption()  {

        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert keyGen != null;
        KeyPair pair = keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
    /**
     *
     * Decrypts a message with the RSA encryption algorithm
     *
     * @param encryptedMessage the message to be decrypted
     * @param privateKey the private key to decrypt the message
     * @return the decrypted string
     */
    public String rsaDecrypt(String encryptedMessage, PrivateKey privateKey){

        byte[] bytes = Base64.getDecoder().decode(encryptedMessage);

        try{
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
            return Base64.getEncoder().encodeToString(decryptCipher.doFinal(bytes));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Encrypts a string using RSA encryption algorithm
     *
     * @param string the string to be encrypted
     * @param publicKey the public key to be used for encrypting
     * @return The encrypted string
     */

    public String rsaEncrypt(byte[] string, PublicKey publicKey) {
        Cipher encryptCipher = null;
        try {
            encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] cipherText = encryptCipher.doFinal(string);

            return Base64.getEncoder().encodeToString(cipherText);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Key keyFromString(String key){
        byte[] publicBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }
}
