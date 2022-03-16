package nodeManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
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
        JSON_DATA = apiGETRequest("http://localhost:8080/getAllNodes");
        publicKeys = new ArrayList<>();
        fillNodes();
    }

    private void fillNodes() throws ParseException, IOException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(JSON_DATA);

        JSONArray publicKeyList = (JSONArray) obj;

        publicKeyList.forEach( node -> parseNodeObject( (JSONObject) node ));
    }

    public PublicKey[] generateCircuit(int amount) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PublicKey[] circuit = new PublicKey[amount];
        for (int i = 0; i < amount; i++) {
            byte[] publicBytes = Base64.getDecoder().decode(getRandomNode());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(keySpec);

            circuit[i] = pubKey;
        }
        return circuit;
    }

    private String getRandomNode(){
        int index = (int)(Math.random() * publicKeys.size());
        return publicKeys.get(index);
    }

    private void parseNodeObject(JSONObject node)
    {
        //Get node within list
        JSONObject nodeObject = (JSONObject) node.get("node");

        //Get node's public key
        String publicKey = (String) nodeObject.get("publicKey");
        publicKeys.add(publicKey);
        System.out.println(publicKey);
    }
}
