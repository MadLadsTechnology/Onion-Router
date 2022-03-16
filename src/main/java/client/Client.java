package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Base64;

import static crypto.EncryptionService.rsaEncrypt;

public class Client {

    //Simple client for now
    public static void main(String[] args) throws Exception {


        //TODO: make api call to get a circuit
        PublicKey[] circuit = ;

        String message = "hello there";

        String encryptedData = layerEncryptMessage(circuit, message);

        //Make api call to get address of first node




        //establishing connection with node
        String ip = "localhost";
        int port = 6969;
        Socket clientSocket = new Socket(ip, port);
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        if (clientSocket.isConnected()){
            System.out.println("Connection Acquired");
        }

        out.write(encryptedData);
        System.out.println("\n" + in.readLine());

        out.close();
        in.close();
        clientSocket.close();

        //TODO: Add more options for communication
    }

    /**
     *
     * @param circuit array of publickeys the message will travel thru
     * @param message the message to be delivered to the endpoint
     * @return a multiencrypted message containing public keys
     * @throws Exception
     */

    private static String layerEncryptMessage(PublicKey[] circuit, String message) throws Exception {

        String layer1 = rsaEncrypt(message.getBytes(), circuit[2]);

        String data2 = publicKeyAsString(circuit[2]) + layer1;

        String layer2 = rsaEncrypt(data2.getBytes(), circuit[1]);

        String data3 = publicKeyAsString(circuit[1]) + layer2;

        return rsaEncrypt(data3.getBytes(), circuit[0]);
    }

    private static String publicKeyAsString(PublicKey key){
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

}
