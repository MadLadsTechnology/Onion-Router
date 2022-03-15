package nodeManager;


import crypto.RSAKeyPairGenerator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

/**
 * Class to start run an instance of a node
 *
 * To be able to handle several requests it uses threads to handle requests
 *
 */
public class NodeMain {

    private String privateKey;

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {


        RSAKeyPairGenerator keyGen = new RSAKeyPairGenerator();
        Node thisNode = new Node(keyGen.getPrivateKey(), keyGen.getPublicKey());

        boolean running = true;

        ServerSocket serverSocket = new ServerSocket(2020);
        while(running){
            Socket socket;

            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(
                        "Error accepting connection", e);
            }

            new Thread(new NodeRunnable(socket, thisNode)).start();
            System.out.println("New connection established");
        }

    }

}
