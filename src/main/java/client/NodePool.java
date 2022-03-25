package client;

import encryption.RSAEncryption;
import model.Node;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;

public class NodePool {
    private final String JSON_DATA;
    private final ArrayList<Node> nodePool = new ArrayList<>();;

    /**
     * Connects to the nodeServerApi and fetches all active nodes in the network
     * @throws Exception
     */
    public NodePool(String JSON_DATA) throws Exception {
        this.JSON_DATA = JSON_DATA;
        parseJsonData();
    }
    /**
     *
     * Fills the arrayList with PublicKeys with the data from the JSON data
     *
     * @throws ParseException if the given data can't be parsed to a json object
     */
    private void parseJsonData() throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject  = (JSONObject) parser.parse(JSON_DATA);

        JSONArray nodesArray = (JSONArray) jsonObject.get("nodes");

        for(Object node : nodesArray){
            nodePool.add(parseNodeObject((JSONObject) node));
        }
    }

    /**
     *
     * @param node the node containing the json object to be parsed
     */
    private Node parseNodeObject(JSONObject node){
        //Get node's public key
        String host = (String) node.get("ip");
        int port = Integer.parseInt(String.valueOf((long) node.get("port")));;

        return new Node( host, port);
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

        Node[] circuit = new Node[circuitArray.size()];

        for(int i = 0; i<circuitArray.size(); i++){
            circuit[i] = circuitArray.get(i);
        }

        fetchAesKeys(circuit);

        return circuit;
    }

    /**
     *
     * @return random node from the publicKeys arrayList
     */
    private Node getRandomNode(){
        int index = (int)(Math.random() * nodePool.size());
        return nodePool.get(index);
    }

    private void fetchAesKeys(Node[] circuit) {

        RSAEncryption rsaEncryption = new RSAEncryption();

        for (Node node : circuit) {
            String host = node.getHost();
            int port = node.getPort();
            try{
                Socket clientSocket = new Socket(host, port);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                if (clientSocket.isConnected()) {
                    System.out.println("Connected to: " + host + ":" + port);
                }

                out.println("key"); //type of connection
                out.println(Base64.getEncoder().encodeToString(rsaEncryption.getPublicKey().getEncoded())); //Sending public key

                String encryptedAESKey = in.readLine(); //Reading encrypted AES key

                String decryptedAESKey = rsaEncryption.rsaDecrypt(encryptedAESKey, rsaEncryption.getPrivateKey());

                byte[] byteKey = Base64.getDecoder().decode(decryptedAESKey); //converting the aes key to bytes

                node.setAesKey(new SecretKeySpec(byteKey, 0, byteKey.length, "AES")); //getting the aes key of the node

                out.close();
                in.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
