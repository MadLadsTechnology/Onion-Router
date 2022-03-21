package client;

import crypto.EncryptionService;
import crypto.RSAKeyPairGenerator;
import node.Node;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;

import static API.APIService.apiGETRequest;

public class NodeHandler {
    private final String JSON_DATA;
    private final ArrayList<Node> nodes;
    private Node[] circuit;

    /**
     * Connects to the nodeServerApi and fetches all active nodes in the network
     * @throws Exception
     */
    public NodeHandler(int circuitLength) throws Exception {
        JSON_DATA = apiGETRequest("http://localhost:8080/api/getAllNodes");
        nodes = new ArrayList<>();
        fillNodes();
        generateCircuit(circuitLength);
        getAesKeys();
    }
    /**
     *
     * Fills the arrayList with PublicKeys with the data from the JSON data
     *
     * @throws ParseException if the given data can't be parsed to a json object
     */
    private void fillNodes() throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject  = (JSONObject) parser.parse(JSON_DATA);

        JSONArray nodesArray = (JSONArray) jsonObject.get("nodes");

        nodesArray.forEach( node -> parseNodeObject( (JSONObject) node ));
    }

    private void getAesKeys() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

        RSAKeyPairGenerator rsaKeyPairGenerator = new RSAKeyPairGenerator();
        EncryptionService encryptionService = new EncryptionService();

        for (Node node : circuit) {
            String host = node.getHost();
            int port = node.getPort();
            Socket clientSocket = new Socket(host, port);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            if (clientSocket.isConnected()) {
                System.out.println("Connected to: " + host + ":" + port);
            }

            out.println("key"); //type of connection
            out.println(Base64.getEncoder().encodeToString(rsaKeyPairGenerator.getPublicKey().getEncoded())); //Sending public key

            String encryptedAESKey = in.readLine(); //Reading encrypted AES key

            String decryptedAESKey = encryptionService.rsaDecrypt(encryptedAESKey, rsaKeyPairGenerator.getPrivateKey());

            byte[] byteKey = Base64.getDecoder().decode(decryptedAESKey); //converting the aes key to bytes

            node.setAesKey(new SecretKeySpec(byteKey, 0, byteKey.length, "AES")); //getting the aes key of the node

            out.close();
            in.close();
            clientSocket.close();
        }
    }

    /**
     *
     * @param amount amount of nodes to be used in the circuit
     * @return the circuit of publicKeys
     */
    public Node[] generateCircuit(int amount) {
        ArrayList<Node> circuitArray = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Node node = getRandomNode();
            if(!circuitArray.contains(node)){
                circuitArray.add(node);
            }else{
                i--;
            }
        }

        circuit = new Node[circuitArray.size()];

        for(int i = 0; i<circuitArray.size(); i++){
            circuit[i] = circuitArray.get(i);
        }
        return circuit;
    }

    /**
     *
     * @return random node from the publicKeys arrayList
     */
    private Node getRandomNode(){
        int index = (int)(Math.random() * nodes.size());
        return nodes.get(index);
    }

    /**
     *
     * @param node the node containing the json object to be parsed
     */
    private void parseNodeObject(JSONObject node){
        //Get node's public key
        String host = (String) node.get("ip");
        int port = Integer.parseInt(String.valueOf((long) node.get("port")));;

        nodes.add(new Node( host, port));
    }

    public Node[] getCircuit() {
        return circuit;
    }

    public void setCircuit(Node[] circuit) {
        this.circuit = circuit;
    }
}
