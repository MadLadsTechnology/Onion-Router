package nodeManager;

import crypto.AESEncryption;
import crypto.EncryptionService;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.Base64;

import static API.APIService.apiGETRequestWithPayload;


/**
 * Class to handle the node logic.
 *
 */

public class NodeThread extends Thread {

    Socket socket;
    Node thisNode;
    public NodeThread(Socket socket, Node thisNode){
        this.socket = socket;
        this.thisNode = thisNode;
    }

    /**
     * Receive a message, check if readable or encrypted
     *
     * if encrypted find next node, if readable send request to server.
     */
    @Override
    public void run() {
        try {
            System.out.println("running");

            //Settig up readers and reads message from other
            InputStreamReader inputStream = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(inputStream);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            String encryptedData = reader.readLine();
            System.out.println("encryptedData: " + encryptedData);

            String encryptedAESKey = reader.readLine();
            System.out.println("encryptedAESKey: " + encryptedAESKey);

            reader.close();
            writer.close();
            inputStream.close();


            EncryptionService encryptionService = new EncryptionService();


            String decryptedAESKey = encryptionService.rsaDecrypt(encryptedAESKey, thisNode.getPrivateKey());

            byte[] decodedKey = decryptedAESKey.getBytes(StandardCharsets.UTF_8);

            // rebuild key using SecretKeySpec
            SecretKey aesKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

            AESEncryption aesEncryption = new AESEncryption();

            String decryptedData = aesEncryption.decrypt(encryptedData, aesKey);

            if(decryptedData.length() < 256){
                System.out.println(decryptedData);
            }

            String publicKey = decryptedData.substring(0,127);

            String encryptedAesKey = decryptedData.substring(128,255);

            String message = decryptedData.substring(256);

            String address = apiGETRequestWithPayload("http://localhost:8080/api/getAddress", publicKey);

            if(!address.equals("")){

                String ip = address.split(":")[0];
                int port = Integer.parseInt(address.split(":")[1]);
                Socket clientSocket = new Socket(ip, port);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                if (clientSocket.isConnected()){
                    System.out.println("Connection Acquired");
                }

                out.println(message);

                out.println(encryptedAesKey);

                System.out.println("\n" + in.readLine());

                out.close();
                in.close();
                clientSocket.close();

            }else{
                System.out.println("apekatter" + message);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
