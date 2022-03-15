package client;

import nodeManager.Node;

import java.io.*;
import java.net.Socket;
import java.security.PublicKey;

public class Client {



    //Simple client for now
    public static void main(String[] args) throws IOException {
        String ip = "localhost";
        int port = 6969;
        Socket clientSocket = new Socket(ip, port);
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        if (clientSocket.isConnected()){
            System.out.println("Connection Acquired");
        }


        out.println("");
        out.println("halla");
        System.out.println("\n" + in.readLine());


        out.close();
        in.close();
        clientSocket.close();

        //TODO: Add more options for communication
    }
/*
    public PublicKey[] getCircuit(){
        File nodeData = new File("src/main/resources/nodeData.json");
        NodeHandler nodes = new NodeHandler(nodeData);
        PublicKey[] circuit = new PublicKey[3];
        circuit[0] = nodes.getRandomNode().getPublicKey();
        circuit[1] = nodes.getRandomNode().getPublicKey();
        circuit[2] = nodes.getRandomNode().getPublicKey();
        return circuit;
    }*/

}
