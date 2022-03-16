package nodeManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Base64;

import static crypto.EncryptionService.rsaDecrypt;
import static crypto.EncryptionService.rsaEncrypt;

/**
 * Class to handle the node logic.
 *
 */

public class NodeThread extends Thread {

    Socket socket;
    int threadNumber;
    Node thisNode;
    String message;

    public NodeThread(Socket socket, Node thisNode){
        this.socket = socket;
        this.threadNumber = threadNumber;
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
            InputStreamReader inputStream = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(inputStream);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            message = reader.readLine();

            reader.close();
            writer.close();
            inputStream.close();

            String decryptedData = rsaDecrypt(message, thisNode.getPrivateKey());

            byte[] decryptedBytes = decryptedData.getBytes();

            if(decryptedBytes.length < 1024){

            }

            byte[] publicKeyBytes = new byte[1024];

            System.arraycopy(decryptedBytes, 0, publicKeyBytes, 0, publicKeyBytes.length);

            String publicKey = Base64.getEncoder().encodeToString(publicKeyBytes);


            if(true){

                byte[] messageBytes = new byte[decryptedBytes.length - publicKeyBytes.length];

                System.arraycopy(decryptedBytes, publicKeyBytes.length, messageBytes, 0, decryptedBytes.length-publicKeyBytes.length);

                String message = Base64.getEncoder().encodeToString(messageBytes);

                //TODO: make nodeServerAPI call to get address of next node based on the public key



                String ip = "localhost";
                int port = 6969;
                Socket clientSocket = new Socket(ip, port);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                if (clientSocket.isConnected()){
                    System.out.println("Connection Acquired");
                }

                out.write(message);
                System.out.println("\n" + in.readLine());

                out.close();
                in.close();
                clientSocket.close();

            }else{

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
