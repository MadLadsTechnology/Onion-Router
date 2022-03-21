package node;

import crypto.AESEncryption;
import crypto.EncryptionService;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
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
        EncryptionService encryptionService = new EncryptionService();
        try {
            //Setting up readers and reads message from other
            InputStreamReader inputStream = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(inputStream);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("connection from: " + socket.getInetAddress() + ":" + socket.getPort());

            //Reading the data from the previous node
            String typeOfMessage = reader.readLine();

            if (typeOfMessage.equals("key")){
                String publicKey = reader.readLine();
                PublicKey pubKey = (PublicKey) encryptionService.keyFromString(publicKey, "RSA");

                String aesKeyEncoded = encryptionService.rsaEncrypt(this.thisNode.getAesKey().getEncoded(), pubKey);

                writer.println(aesKeyEncoded);

                reader.close();
                writer.close();
                inputStream.close();
            }
            else{
                String encryptedData = reader.readLine();
                String encryptedAESKey = reader.readLine();

                //closing connection
                reader.close();
                writer.close();
                inputStream.close();

                //Decrypting the AES key needed to decrypt the data.
                String decryptedAESKey = encryptionService.rsaDecrypt(encryptedAESKey, thisNode.getPrivateKey());

                //Rebuilding the AES key from the string
                byte[] decodedKey = Base64.getDecoder().decode(decryptedAESKey);
                SecretKey aesKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

                //Decrypting the data with the AES key
                AESEncryption aesEncryption = new AESEncryption();
                String decryptedData = aesEncryption.decrypt(encryptedData, aesKey);

                if(decryptedData.length() <= 348){
                    System.out.println(decryptedData);
                }else{

                    //Creating substrings of the decrypted data into their actual forms
                    String[] dataSplit = decryptedData.split(":", 2);


                    String host = dataSplit[0];
                    String addressPort = dataSplit[1].substring(0, 4);
                    String data = dataSplit[1].substring(4);

                    if(!host.equals(":")){ //if address is found

                        //creating connection to next node
                        int port = Integer.parseInt(addressPort);
                        System.out.println("Connecting to next node: " + host + ":" + port);
                        Socket clientSocket = new Socket(host, port);
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                        if (clientSocket.isConnected()){
                            System.out.println("Connection acquired");

                            //Sending encrypted data to next node
                            out.println(data);
                        }

                        //Closing connection
                        out.close();
                        in.close();
                        clientSocket.close();

                    }else{
                        System.out.println( data);
                    }
                }

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
