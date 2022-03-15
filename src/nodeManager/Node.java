package nodeManager;


/**
 * Class to represent a node.
 */
public class Node {
    
    private String privateKey;  
    public String publicKey;
    private String nextNodeAddress;


     public Node(){

     }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getNextNodeAddress() {
        return nextNodeAddress;
    }

    public void setNextNodeAddress(String nextNodeAddress) {
        this.nextNodeAddress = nextNodeAddress;
    }
}
