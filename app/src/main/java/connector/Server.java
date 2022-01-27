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
        this.start();
    }

    public void Send(ProtocolMessage message) {
        connected.removeIf(DataClient::isClosed);
        for (DataClient client : connected) {
            Send(client, message);
        }
    }

    public void Send(DataClient client, ProtocolMessage message) {
        try {
            client.STREAM.writeObject(message);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(ConnectionSettings.PORT)) {
            System.out.println("Server is listening on port " + ConnectionSettings.PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                new StreamReader(new ObjectInputStream(socket.getInputStream()), eventHandler).start();
                var dataClient = new DataClient(outputStream, socket);
                connected.add(dataClient);
                Send(dataClient, new ProtocolMessage());
                eventHandler.onNewConnection(dataClient);
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


}
