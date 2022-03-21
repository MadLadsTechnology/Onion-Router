package client;

import node.Node;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import static API.APIService.apiGETRequest;

public class NodeHandler {
    private final String JSON_DATA;
    private final ArrayList<Node> nodes;

    /**
     * Connects to the nodeServerApi and fetches all active nodes in the network
     * @throws Exception
     */
    public NodeHandler() throws Exception {
        JSON_DATA = apiGETRequest("http://localhost:8080/api/getAllNodes");
        nodes = new ArrayList<>();
        fillNodes();
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

    /**
     *
     * @param amount amount of nodes to be used in the circuit
     * @return the circuit of publicKeys
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public Node[] generateCircuit(int amount) {
        ArrayList<Node> circuit = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Node node = getRandomNode();
            if(!circuit.contains(node)){
                circuit.add(node);
            }else{
                i--;
            }
        }
        Node[] nodesArray = new Node[circuit.size()];

        for(int i = 0; i<circuit.size(); i++){
            nodesArray[i] = circuit.get(i);
        }
        return nodesArray;
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
}
