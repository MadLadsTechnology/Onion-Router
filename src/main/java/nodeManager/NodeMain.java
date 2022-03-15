package nodeManager;

import crypto.RSAKeyPairGenerator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * Class to start run an instance of a node
 *
 * To be able to handle several requests it uses threads to handle requests
 *
 */
public class NodeMain {


    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        RSAKeyPairGenerator keyGen = new RSAKeyPairGenerator();
        Node thisNode = new Node(keyGen.getPrivateKey(), keyGen.getPublicKey());

        Scanner in = new Scanner(System.in);
        System.out.println("Please specify your wanted port:");
        int PORT = Integer.parseInt(in.nextLine());




        JSONObject jsonObject = new JSONObject();

        jsonObject.put("publicKey", thisNode.getPublicKey().toString());
        jsonObject.put("port", PORT);





        boolean running = true;

        ServerSocket serverSocket = new ServerSocket(PORT);
        while(running){
            Socket socket;

            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(
                        "Error accepting connection", e);
            }

            new Thread(new NodeThread(socket, thisNode)).start();
            System.out.println("New connection established");
        }

    }

    private void writeToJson(JSONObject o)  {
        try {
            FileReader reader = new FileReader("./src/main/resources/nodeData.json");

            Object readObject
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        JSONParser parser = new JSONParser();



        try (FileWriter file = new FileWriter("./src/main/resources/nodeData.json")) {
            //We can write any JSONArray or JSONObject instance to the file
            file.write(jsonObject.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
