package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {



    //Simple client for now
    public static void main(String[] args) throws IOException {
        String ip = "localhost";
        int port = 6969;
        Socket clientSocket = new Socket(ip, port);
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        if (clientSocket.isConnected()){
            System.out.println("Connection Acquired");
        }


        out.println("");
        out.println("halla");
        System.out.println("\n" + in.readLine());


        out.close();
        in.close();
        clientSocket.close();

        //TODO: Add more options for communication
    }


}
