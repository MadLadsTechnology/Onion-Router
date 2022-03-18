package client;

import crypto.AESEncryption;
import crypto.EncryptionService;
import node.Node;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.Key;
import java.security.PublicKey;
import java.util.Base64;

import static API.APIService.apiGETRequestWithPayload;


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
        NodeHandler nodeHandler = new NodeHandler();
        Node[] circuit = nodeHandler.generateCircuit(3);

        String message = "Hello there, you are reading this on the last node!";

        String[] onion = layerEncryptMessage(circuit, message);

        //establishing connection with node
        String host = circuit[0].getHost();
        int port = circuit[0].getPort();
        Socket clientSocket = new Socket(host, port);
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        if (clientSocket.isConnected()){
            System.out.println("Connected to: " + host + ":" + port);
        }

        //sending data
        out.println(onion[0]); //data
        out.println(onion[1]); //encrypted AES key

        out.close();
        in.close();
        clientSocket.close();

    }

    /**
     *
     * Takes a message and encrypts it to be sent through a circuit of nodes in the node network
     *
     * @param circuit array of publickeys the message will travel thru
     * @param message the message to be delivered to the endpoint
     * @return a multiencrypted message containing public keys
     * @throws Exception
     */

    private static String[] layerEncryptMessage(Node[] circuit, String message) throws Exception {

        EncryptionService encryptionService = new EncryptionService();
        AESEncryption aesEncryption = new AESEncryption();

        String data = message;

        for(int i=1;i < circuit.length; i++){

            //creating AES key and encrypting data with it
            SecretKey aesKey = aesEncryption.getAESKey();

            String encryptedMessage1 = aesEncryption.encrypt(data, aesKey);

            //RSA encrypts the AES key
            String rsaEncryptedAesKey1 = encryptionService.rsaEncrypt(aesKey.getEncoded(), circuit[i].getPublicKey());

            //Setting up string to hold data for next layer
            data =  circuit[i].getHost() + ":"  + circuit[i].getPort() + rsaEncryptedAesKey1 + encryptedMessage1 ;

        }

        String[] onion = new String[2];

        SecretKey aesKey = aesEncryption.getAESKey();

        onion[0] = aesEncryption.encrypt(data, aesKey);

        onion[1] = encryptionService.rsaEncrypt(aesKey.getEncoded(), circuit[0].getPublicKey());

        return onion;

    }

    /**
     *
     * Method to get the string value of publicKey object
     *
     * @param key the key object you want as a string
     * @return the given key as a string
     */
    private static String publicKeyAsString(Key key){
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
