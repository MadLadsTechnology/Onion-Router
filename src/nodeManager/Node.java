package nodeManager;


import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Class to represent a node.
 */
public class Node {
    
    private PrivateKey privateKey;
    public PublicKey publicKey;
    private String nextNodeAddress;


     public Node(PublicKey publicKey){
         this.publicKey = publicKey;

     }
    public Node(PrivateKey privateKey, PublicKey publicKey){
         this.privateKey = privateKey;
         this.publicKey = publicKey;
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

    public String getNextNodeAddress() {
        return nextNodeAddress;
    }

    public void setNextNodeAddress(String nextNodeAddress) {
        this.nextNodeAddress = nextNodeAddress;
    }
}
