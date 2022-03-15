package nodeManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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

            }

            reader.close();
            writer.close();
            inputStream.close();
        } catch ( IOException e) {
            e.printStackTrace();
        }


    }


}
