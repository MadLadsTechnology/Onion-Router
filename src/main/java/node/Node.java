package node;


import javax.crypto.SecretKey;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Class to represent a node.
 */
public class Node {
    
    private PrivateKey privateKey;
    public PublicKey publicKey;
    private SecretKey aesKey;
    private String host;
    private int port;

    public Node(SecretKey aesKey, String host, int port){
        this.aesKey = aesKey;
        this.host = host;
        this.port = port;
    }
    public Node(SecretKey aesKey){
        this.aesKey = aesKey;
    }

    public Node(PublicKey publicKey, String host, int port){
         this.publicKey = publicKey;
         this.host = host;
         this.port = port;
    }
    public Node( String host, int port){
        this.host = host;
        this.port = port;
    }
    public Node(String publicKeyAsString, String host, int port) {
        this.publicKey = publicKeyFromString(publicKeyAsString);
        this.host = host;
        this.port = port;
    }
    public Node(PrivateKey privateKey, PublicKey publicKey){
         this.privateKey = privateKey;
         this.publicKey = publicKey;
    }
    public Node(PrivateKey privateKey, String publicKeyAsString)  {
        this.privateKey = privateKey;
        this.publicKey = publicKeyFromString(publicKeyAsString);
    }

    private PublicKey publicKeyFromString(String stringKey)  {
         try {
             byte[] publicBytes = Base64.getDecoder().decode(stringKey);
             X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
             KeyFactory keyFactory = KeyFactory.getInstance("RSA");
             return keyFactory.generatePublic(keySpec);
         } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
             e.printStackTrace();
             return null;
         }
    }

    public SecretKey getAesKey() {
        return aesKey;
    }

    public void setAesKey(SecretKey aesKey) {
        this.aesKey = aesKey;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
}
