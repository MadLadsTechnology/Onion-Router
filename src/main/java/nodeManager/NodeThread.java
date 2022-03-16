package nodeManager;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import static crypto.encryptionService.rsaDecrypt;
import static crypto.encryptionService.rsaEncrypt;

/**
 * Class to handle the node logic.
 *
 */

public class NodeThread extends Thread {

    //TODO: Add implementation as a thread maybe

    Socket socket;
    int threadNumber;
    Node thisNode;
    Node nextNode;
    String message;
    boolean messageIsEncrypted;

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

            //Todo: check if message is encrypted

            if(messageIsEncrypted){
                message = rsaDecrypt(message, thisNode.getPrivateKey());
            } else{
                message = rsaEncrypt(message.getBytes(), thisNode.getPublicKey());
            }

            reader.close();
            writer.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
