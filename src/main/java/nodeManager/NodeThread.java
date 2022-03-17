package nodeManager;

import crypto.AESEncryption;
import crypto.EncryptionService;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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

            //Reading the data from the previous node
            String encryptedData = reader.readLine();
            String encryptedAESKey = reader.readLine();

            //closing connection
            reader.close();
            writer.close();
            inputStream.close();

            EncryptionService encryptionService = new EncryptionService();

            //Decrypting the AES key needed to decrypt the data.
            String decryptedAESKey = encryptionService.rsaDecrypt(encryptedAESKey, thisNode.getPrivateKey());

            //Rebuilding the AES key from the string
            byte[] decodedKey = Base64.getDecoder().decode(decryptedAESKey);
            SecretKey aesKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

            //Decrypting the data with the AES key
            AESEncryption aesEncryption = new AESEncryption();
            String decryptedData = aesEncryption.decrypt(encryptedData, aesKey);


            if(decryptedData.length() <= 736){
                System.out.println(decryptedData);
            }else{

                //Creating substrings of the decrypted data into their actual forms
                String publicKey = decryptedData.substring(0,392);
                String encryptedAesKey = decryptedData.substring(392,736);
                String message = decryptedData.substring(736);


                //Requesting the address of the next node, based on the public key in the decrypted data
                String address = apiGETRequestWithPayload("http://localhost:8080/api/getAddress", publicKey);

                if(!address.equals("")){ //if address is found

                    //creating connection to next node
                    String ip = address.split(":")[0];
                    int port = Integer.parseInt(address.split(":")[1]);
                    Socket clientSocket = new Socket(ip, port);
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    if (clientSocket.isConnected()){
                        System.out.println("Connection to next node acquired");

                        //Sending encrypted data to next node
                        out.println(message);
                        out.println(encryptedAesKey);
                    }


                   //Closing connection
                    out.close();
                    in.close();
                    clientSocket.close();

                }else{
                    System.out.println( message);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
