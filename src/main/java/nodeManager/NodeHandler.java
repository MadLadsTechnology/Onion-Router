package nodeManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;

import static API.APIService.apiGETRequest;

public class NodeHandler {
    private final String JSON_DATA;
    private ArrayList<String> publicKeys;

    public NodeHandler() throws Exception {
        JSON_DATA = apiGETRequest("http://localhost:8080/api/getAllNodes");
        publicKeys = new ArrayList<>();
        fillNodes();
    }

    /***
     *
     * @param publicKeys
     */
    public NodeHandler(ArrayList<String> publicKeys) {
        JSON_DATA = null;
        this.publicKeys = publicKeys;
    }

    /**
     *
     * @throws ParseException
     */
    private void fillNodes() throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject  = (JSONObject) parser.parse(JSON_DATA);

        JSONArray publicKeyList = (JSONArray) jsonObject.get("nodes");

        publicKeyList.forEach( node -> parseNodeObject( (JSONObject) node ));
    }

    /**
     *
     * @param amount
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public PublicKey[] generateCircuit(int amount) throws NoSuchAlgorithmException, InvalidKeySpecException {
        ArrayList<PublicKey> circuit = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            byte[] publicBytes = Base64.getDecoder().decode(getRandomNode());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(keySpec);
            if(!circuit.contains(pubKey)){
                circuit.add(pubKey);
            }else{
                i--;
            }
        }

        PublicKey[] keys = new PublicKey[circuit.size()];

        for(int i = 0; i<circuit.size(); i++){
            keys[i] = circuit.get(i);
        }
        return keys;
    }

    /**
     *
     * @return
     */
    private String getRandomNode(){
        int index = (int)(Math.random() * publicKeys.size());
        return publicKeys.get(index);
    }

    /**
     *
     * @param node
     */
    private void parseNodeObject(JSONObject node)
    {
        //Get node's public key
        String publicKey = (String) node.get("publicKey");
        publicKeys.add(publicKey);
    }
}
