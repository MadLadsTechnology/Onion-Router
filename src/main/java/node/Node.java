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

    private SecretKey aesKey;
    private String host;
    private int port;

    public Node(SecretKey aesKey){
        this.aesKey = aesKey;
    }

    public Node( String host, int port){
        this.host = host;
        this.port = port;
    }
    public Node(SecretKey aesKey, String host, int port){
        this.aesKey = aesKey;
        this.host = host;
        this.port = port;
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

}
