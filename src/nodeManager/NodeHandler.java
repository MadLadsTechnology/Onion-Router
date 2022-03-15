package nodeManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


public class NodeHandler{


    HashMap<String, Node> listOfAllNodes;


    public NodeHandler(){
        listOfAllNodes = new HashMap<>();
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
