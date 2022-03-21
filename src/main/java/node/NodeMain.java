package node;

import crypto.AESEncryption;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import static API.APIService.apiDELETENode;
import static API.APIService.apiPOSTNode;

/**
 * Class to start run an instance of a node
 *
 * To be able to handle several requests it uses threads to handle requests
 *
 */
public class NodeMain {


    public static void main(String[] args) throws Exception {
        AESEncryption aesEncryption = new AESEncryption();
        SecretKey aesKey = aesEncryption.getAESKey();

        Scanner in = new Scanner(System.in);
        System.out.println("Please specify your wanted port:");
        int PORT = Integer.parseInt(in.nextLine());

        Node thisNode = new Node(aesKey, "localhost", PORT);

        int responseCode = apiPOSTNode("http://localhost:8080/api/putNode", "localhost:" + PORT);

        System.out.println("The server responded with:" + responseCode);

        Thread thread = new Thread(() -> {
            try {
                apiDELETENode("http://localhost:8080/api/deleteNode", "localhost:" + PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Runtime.getRuntime().addShutdownHook(thread);

        ServerSocket serverSocket = new ServerSocket(PORT);
        do {
            Socket socket;

            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(
                        "Error accepting connection", e);
            }

            new Thread(new NodeThread(socket, thisNode)).start();
        } while (true);
    }
}
