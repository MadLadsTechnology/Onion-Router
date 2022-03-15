package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public static void main(String[] args) throws IOException{
        int port = 6969;
        ServerSocket serverSocket = new ServerSocket(port);

        Socket socket = serverSocket.accept();
        System.out.println("Connection created");

        InputStream input = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line = reader.readLine();
        System.out.println(line);

        OutputStream output = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(output, true);
        writer.println("This is a message sent to the server");

        socket.close();
        System.out.println("Connection is closing");
    }
}
