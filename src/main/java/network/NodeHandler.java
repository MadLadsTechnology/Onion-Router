package network;

import nodeManager.Node;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


public class NodeHandler{
    private File nodeData;
    HashMap<String, Node> listOfAllNodes;


    public NodeHandler(File nodeData){
        this.nodeData = nodeData;
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
