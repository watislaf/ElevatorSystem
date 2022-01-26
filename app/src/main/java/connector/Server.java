package connector;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class Server extends Thread {
    final OnReceive receivable;

    LinkedList<Socket> connected;

    public Server(OnReceive receivable) {
        this.receivable = receivable;
        connected = new LinkedList<Socket>();
        this.start();
    }


    public void Send(String message) {
        connected.removeIf(Socket::isClosed);
        for (Socket socket : connected) {
            try {
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);
                writer.println(message);
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(ConnectionSettings.PORT)) {
            System.out.println("Server is listening on port " + ConnectionSettings.PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                connected.add(socket);
                new StreamReader(socket.getInputStream(), receivable).start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


}
