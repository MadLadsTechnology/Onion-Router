package node;

import crypto.AESEncryption;
import crypto.EncryptionService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.PublicKey;

import static API.APIService.apiGETRequest;


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
            InputStreamReader firstInputStream = new InputStreamReader(socket.getInputStream());
            BufferedReader firstReader = new BufferedReader(firstInputStream);
            PrintWriter firstWriter = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("connection from: " + socket.getInetAddress() + ":" + socket.getPort());

            //Reading the data from the previous node
            String typeOfMessage = firstReader.readLine();

            if (typeOfMessage.equals("key")){
                String publicKey = firstReader.readLine();
                PublicKey pubKey = (PublicKey) encryptionService.keyFromString(publicKey, "RSA");

                String aesKeyEncoded = encryptionService.rsaEncrypt(this.thisNode.getAesKey().getEncoded(), pubKey);

                firstWriter.println(aesKeyEncoded);

                firstReader.close();
                firstWriter.close();
                firstInputStream.close();
            }
            else{
                String encryptedData = firstReader.readLine();

                //closing connection



                //Decrypting the data with the AES key
                AESEncryption aesEncryption = new AESEncryption();
                System.out.println("received: " + encryptedData);
                String decryptedData = aesEncryption.decrypt(encryptedData, thisNode.getAesKey());

                if(!decryptedData.contains("localhost")){
                    firstWriter.println(apiGETRequest(decryptedData));
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
                        PrintWriter secondWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                        BufferedReader secondReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                        if (clientSocket.isConnected()){
                            System.out.println("Connection acquired");

                            System.out.println("sent: " + data);
                            //Sending encrypted data to next node
                            secondWriter.println("not a key");
                            secondWriter.println(data);
                        }

                        firstWriter.println(secondReader.readLine());

                        //Closing connection
                        secondWriter.close();
                        secondReader.close();
                        clientSocket.close();

                        firstReader.close();
                        firstWriter.close();
                        firstInputStream.close();

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
