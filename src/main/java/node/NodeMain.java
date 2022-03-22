package node;

import encryption.AESEncryption;
import model.Node;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.InetAddress;
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
        System.out.println("Please specify the port you want to host the node from:");
        int PORT = Integer.parseInt(in.nextLine());
        System.out.println("Please specify the address of the server:");
        String serverAddress = in.nextLine();

        String address = InetAddress.getLocalHost().getHostAddress();

        Node thisNode = new Node(aesKey, address, PORT);

        int responseCode = apiPOSTNode("http://" + serverAddress  + ":8080/api/putNode", address +":" + PORT);
        System.out.println("The server responded with:" + responseCode);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                apiDELETENode("http://"+serverAddress +"/api/deleteNode", address +":" + PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        ServerSocket serverSocket = new ServerSocket(PORT);
        while(true) {
            Socket socket;

            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(
                        "Error accepting connection", e);
            }

            new Thread(new NodeThread(socket, thisNode)).start();
        }
    }
}
