package nodeManager;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class to start run an instance of a node
 *
 * To be able to handle several requests it uses threads to handle requests
 *
 *
 */
public class NodeMain {

    private String privateKey;

    public static void main(String[] args) throws IOException {

        boolean running = true;
        int i = 0;

        ServerSocket serverSocket = new ServerSocket(1250);
        while(running){
            Socket socket;

            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }


            new Thread(new NodeRunnable(socket)).start();
            System.out.println("Ny klient tilkoblet");
        }

    }

}
