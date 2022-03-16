package nodeManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Iterator;

import static API.APIService.apiGETRequest;

public class NodeHandler {
    private final String JSON_DATA;
    private HashMap<PublicKey, Node> nodes;

    public NodeHandler() throws Exception {
        JSON_DATA = apiGETRequest("http://localhost:8080/getAllNodes");
        fillNodes();
    }

    private void fillNodes() throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(JSON_DATA);
        HashMap<String,Object> result = new ObjectMapper().readValue(obj, HashMap.class);
        System.out.println(result.get(0).toString());


    }

    public PublicKey[] generateCircuit(){

    }

    private Node getRandomNode(){

    }
}
