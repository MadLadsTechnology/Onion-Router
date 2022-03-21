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
        NodeHandler nodeHandler = new NodeHandler();
        Node[] circuit = nodeHandler.generateCircuit(3);

        EncryptionService encryptionService = new EncryptionService();

        RSAKeyPairGenerator rsaKeyPairGenerator = new RSAKeyPairGenerator();


        for (int i = 0; i < circuit.length; i++) {
            String host = circuit[i].getHost();
            int port = circuit[i].getPort();
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
            byte[] byteKey = Base64.getDecoder().decode(decryptedAESKey);

            circuit[i].setAesKey(new SecretKeySpec(byteKey, 0, byteKey.length, "AES")) ; //getting the aes key of the node

            out.close();
            in.close();
            clientSocket.close();
        }

        String message = "https://insult.mattbas.org/api/insult";

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
        out.println("not a key");
        out.println(packet); //data

        //receiving and printing response

        String encryptedResponse = in.readLine();

        System.out.println(decryptOnion(circuit, encryptedResponse));

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

        for(int i=circuit.length-1;i > 0; i--){

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

    private static String decryptOnion(Node[] circuit, String string){

        AESEncryption aesEncryption = new AESEncryption();
        for(int i = 0; i< circuit.length; i++){
            System.out.println(keyAsString(circuit[i].getAesKey()));
            string = aesEncryption.decrypt(string, circuit[i].getAesKey());
        }

        return string;

    }

}
