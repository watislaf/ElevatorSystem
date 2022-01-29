package connector;

import connector.protocol.ProtocolMessage;
import lombok.AllArgsConstructor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server extends Thread {
    final OnSocketEvent eventHandler;
    LinkedList<DataClient> connected;

    public Server(OnSocketEvent eventHandler) {
        this.eventHandler = eventHandler;
        connected = new LinkedList<DataClient>();
    }

    public void Send(ProtocolMessage message) {
        connected.removeIf(DataClient::isClosed);
        if (connected == null) {
            return;
        }
        for (DataClient client : connected) {
            Send(client, message);
        }
    }

    public void Send(DataClient client, ProtocolMessage message) {
        try {
            new ObjectOutputStream(client.STREAM).writeObject(message);
        } catch (IOException e) {
            System.out.println(e.toString());
            System.out.println("XD");
        }
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(ConnectionSettings.PORT)) {
            System.out.println("Server is listening on port " + ConnectionSettings.PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                var dataClient = new DataClient(socket.getOutputStream(), socket);
                new StreamReader(socket, socket.getInputStream(), eventHandler).start();
                connected.add(dataClient);
                eventHandler.onNewConnection(dataClient);
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


}
