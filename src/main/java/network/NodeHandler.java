package network;

import nodeManager.Node;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


public class NodeHandler{
    HashMap<String, Node> listOfAllNodes;


    public NodeHandler(){
        listOfAllNodes = new HashMap<>();
    }

    public void setNode(Node node){
        listOfAllNodes.put(node.getPublicKey().toString(), node);
    }

    public Node getNode(String key){
        return listOfAllNodes.get(key);
    }
    public Node getRandomNode(){
        Set<String> keySet = listOfAllNodes.keySet();
        ArrayList<String> keyList = new ArrayList<>(keySet);
        int randomKey = (int) (Math.random()*listOfAllNodes.size());

        return listOfAllNodes.get(keyList.get(randomKey));
    }

}
