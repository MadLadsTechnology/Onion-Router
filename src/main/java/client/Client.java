package client;

import encryption.AESEncryption;
import model.Node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static API.APIService.apiGETRequest;


/**
 *
 * Client class for an onion router
 *
 * Connects to the node network and sends a message thru it.
 *
 */
public class Client {


    public static void main(String[] args)  {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please specify the address of the nodeServer:");
        String serverAddress = scanner.nextLine();

        System.out.println("Please specify the address of the api you want to fetch from:");
        String apiAddress = scanner.nextLine();


        //Generating a node circuit to send encrypted message
        NodePool nodePool = null;
        try {
            nodePool = new NodePool(apiGETRequest("http://" + serverAddress +":8080/api/getAllNodes"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not connect to server");
            return;
        }
        Node[] circuit = nodePool.generateCircuit(3);

        String message = "lastNode-" + apiAddress; //API we want to call

        String encryptedRequest = layerEncryptMessage(circuit, message);

        //Establishing connection with first node
        String host = circuit[0].getHost();
        int port = circuit[0].getPort();
        try {
            Socket clientSocket = new Socket(host, port);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            if (clientSocket.isConnected()){
                System.out.println("Connected to: " + host + ":" + port);

                //sending data
                out.println("not a key");
                out.println(encryptedRequest); //data
                //receiving and printing response
                String encryptedResponse = in.readLine();

                System.out.println(layerDecryptMessage(circuit, encryptedResponse));
            }

            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * Takes a message and encrypts it in layers to be sent through a circuit of nodes in the node network
     *
     * @param circuit array of nodes the message will travel through
     * @param message the message to be delivered to the endpoint
     * @return a multi-encrypted message containing public keys
     */

    private static String layerEncryptMessage(Node[] circuit, String message) {

        AESEncryption aesEncryption = new AESEncryption();
        String data = message;
        for(int i=circuit.length-1;i > 0; i--){

            //creating AES key and encrypting data with it
            String encryptedMessage1 = aesEncryption.encrypt(data, circuit[i].getAesKey());

            //Setting up string to hold data for next layer
            data =  circuit[i].getHost() + ":"  + circuit[i].getPort() +":"+ encryptedMessage1 ;
        }

        return aesEncryption.encrypt(data, circuit[0].getAesKey());
    }

    private static String layerDecryptMessage(Node[] circuit, String string){

        AESEncryption aesEncryption = new AESEncryption();
        for(int i = 0; i< circuit.length; i++){
            string = aesEncryption.decrypt(string, circuit[i].getAesKey());
        }

        return string;

    }

}
