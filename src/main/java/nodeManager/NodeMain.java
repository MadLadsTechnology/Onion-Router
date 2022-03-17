package nodeManager;

import crypto.RSAKeyPairGenerator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.Scanner;

import static API.APIService.apiPOSTNode;

/**
 * Class to start run an instance of a node
 *
 * To be able to handle several requests it uses threads to handle requests
 *
 */
public class NodeMain {


    public static void main(String[] args) throws Exception {

        RSAKeyPairGenerator keyGen = new RSAKeyPairGenerator();
        Node thisNode = new Node(keyGen.getPrivateKey(), keyGen.getPublicKey());

        String publicKeyAsString = Base64.getEncoder().encodeToString(thisNode.publicKey.getEncoded());

        Scanner in = new Scanner(System.in);
        System.out.println("Please specify your wanted port:");
        int PORT = Integer.parseInt(in.nextLine());

        apiPOSTNode("http://localhost:8080/api/putNode", publicKeyAsString, "localhost:" + PORT);

        boolean running = true;

        ServerSocket serverSocket = new ServerSocket(PORT);
        while(running){
            Socket socket;

            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(
                        "Error accepting connection", e);
            }

            new Thread(new NodeThread(socket, thisNode)).start();
            System.out.println("New connection started from remote");
        }

    }
}
