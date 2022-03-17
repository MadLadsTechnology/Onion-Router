package client;

import crypto.AESEncryption;
import crypto.EncryptionService;
import nodeManager.NodeHandler;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.Key;
import java.security.PublicKey;
import java.util.Base64;

import static API.APIService.apiGETRequestWithPayload;


public class Client {
    //Simple client for now
    public static void main(String[] args) throws Exception {


        //TODO: make api call to get a circuit

        NodeHandler nodeHandler = new NodeHandler();

        PublicKey[] circuit = nodeHandler.generateCircuit(1);

        String message = "hello there";

        String[] onion = layerEncryptMessage(circuit, message);

        System.out.println(publicKeyAsString(circuit[0]));

        String address = apiGETRequestWithPayload("http://localhost:8080/api/getAddress", publicKeyAsString(circuit[0]));

        System.out.println(address);
        String ip = address.split(":")[0];
        int port = Integer.parseInt(address.split(":")[1]);
        //Make api call to get address of first node

        //establishing connection with node
        Socket clientSocket = new Socket(ip, port);
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        if (clientSocket.isConnected()){
            System.out.println("Connection Acquired");
        }

        //sending data
        out.println(onion[0]);

        //sending aesKey
        out.println(onion[1]);


        System.out.println("\n" + in.readLine());

        out.close();
        in.close();
        clientSocket.close();

    }

    /**
     *
     * @param circuit array of publickeys the message will travel thru
     * @param message the message to be delivered to the endpoint
     * @return a multiencrypted message containing public keys
     * @throws Exception
     */

    private static String[] layerEncryptMessage(PublicKey[] circuit, String message) throws Exception {

        EncryptionService encryptionService = new EncryptionService();
        AESEncryption aesEncryption = new AESEncryption();

        String data = message;

        for(int i = circuit.length-1; i>0; i--){
            SecretKey aesKey = aesEncryption.getAESKey();

            String encryptedMessage1 = aesEncryption.encrypt(data, aesKey);

            String rsaEncryptedAesKey1 = encryptionService.rsaEncrypt(aesKey.getEncoded(), circuit[i]);

            data =  publicKeyAsString(circuit[i-1]) + rsaEncryptedAesKey1 + encryptedMessage1 ;

        }

        String[] onion = new String[2];

        SecretKey aesKey = aesEncryption.getAESKey();

        onion[0] = aesEncryption.encrypt(data, aesKey);

        onion[1] = encryptionService.rsaEncrypt(aesKey.getEncoded(), circuit[circuit.length-1]);

        return onion;

    }

    private static String publicKeyAsString(Key key){
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
