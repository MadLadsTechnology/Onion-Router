package client;

import crypto.AESEncryption;
import crypto.EncryptionService;
import crypto.RSAKeyPairGenerator;
import node.Node;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
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

        EncryptionService encryptionService = new EncryptionService();

        RSAKeyPairGenerator rsaKeyPairGenerator = new RSAKeyPairGenerator();

        for (int i = 0; i < circuit.length; i++) {
            String host = circuit[0].getHost();
            int port = circuit[0].getPort();
            Socket clientSocket = new Socket(host, port);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            if (clientSocket.isConnected()){
                System.out.println("Connected to: " + host + ":" + port);
            }

            out.println("key"); //type of connection
            out.println(keyAsString(rsaKeyPairGenerator.getPublicKey())); //Sending public key

            String encryptedAESKey = in.readLine();

            String decryptedAESKey = encryptionService.rsaDecrypt(encryptedAESKey, rsaKeyPairGenerator.getPrivateKey());

            circuit[i].setAesKey((SecretKey) keyFromString(decryptedAESKey)) ; //getting the aes key of the node

            out.close();
            in.close();
            clientSocket.close();
        }

        String message = "Hello there, you are reading this on the last node!";

        String packet = layerEncryptMessage(circuit, message);

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
        out.println(packet); //data

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

    private static String layerEncryptMessage(Node[] circuit, String message) throws Exception {

        AESEncryption aesEncryption = new AESEncryption();

        String data = message;

        for(int i=1;i < circuit.length; i++){

            //creating AES key and encrypting data with it
            String encryptedMessage1 = aesEncryption.encrypt(data, circuit[i].getAesKey());

            //Setting up string to hold data for next layer
            data =  circuit[i].getHost() + ":"  + circuit[i].getPort() + encryptedMessage1 ;
        }

        return aesEncryption.encrypt(data, circuit[0].getAesKey());

    }

    /**
     *
     * Method to get the string value of publicKey object
     *
     * @param key the key object you want as a string
     * @return the given key as a string
     */
    private static String keyAsString(Key key){
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    private static Key keyFromString(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("AES");
        return keyFactory.generatePublic(keySpec);
    }
}
