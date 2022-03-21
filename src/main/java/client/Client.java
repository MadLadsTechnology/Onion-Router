package client;

import crypto.AESEncryption;
import crypto.EncryptionService;
import crypto.RSAKeyPairGenerator;
import node.Node;

import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.Key;
import java.util.Base64;
import java.util.List;


/**
 *
 * Client class for an onion router
 *
 * Connects to the node network and sends a message thru it.
 *
 */
public class Client {


    public static void main(String[] args) throws Exception {

        //Generating a node circuit to send encrypted message
        NodeHandler nodeHandler = new NodeHandler(3);
        Node[] circuit = nodeHandler.getCircuit();

        String message = "https://insult.mattbas.org/api/insult"; //API call we want to do

        String encryptedRequest = layerEncryptMessage(circuit, message);

        //Establishing connection with first node
        String host = circuit[0].getHost();
        int port = circuit[0].getPort();
        Socket clientSocket = new Socket(host, port);
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        if (clientSocket.isConnected()){
            System.out.println("Connected to: " + host + ":" + port);
        }

        //sending data
        out.println("not a key");
        out.println(encryptedRequest); //data

        //receiving and printing response
        String encryptedResponse = in.readLine();

        System.out.println(layerDecryptMessage(circuit, encryptedResponse));

        out.close();
        in.close();
        clientSocket.close();
    }

    /**
     *
     * Takes a message and encrypts it in layers to be sent through a circuit of nodes in the node network
     *
     * @param circuit array of nodes the message will travel through
     * @param message the message to be delivered to the endpoint
     * @return a multiencrypted message containing public keys
     * @throws Exception
     */

    private static String layerEncryptMessage(Node[] circuit, String message) throws Exception {

        AESEncryption aesEncryption = new AESEncryption();
        String data = message;
        for(int i=circuit.length-1;i > 0; i--){

            //creating AES key and encrypting data with it
            String encryptedMessage1 = aesEncryption.encrypt(data, circuit[i].getAesKey());

            //Setting up string to hold data for next layer
            data =  circuit[i].getHost() + ":"  + circuit[i].getPort() + encryptedMessage1 ;
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
