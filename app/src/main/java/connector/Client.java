package connector;

import connector.protocol.Protocol;
import connector.protocol.ProtocolMessage;

import java.io.*;
import java.net.Socket;
import java.rmi.UnknownHostException;

public class Client {
    private ObjectOutputStream outputStream;
    private Socket SOCKET;

    public void Send(ProtocolMessage message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public Client(String hostname, OnSocketEvent receivable) {
        try {
            SOCKET = new Socket(hostname, ConnectionSettings.PORT);
            outputStream = new ObjectOutputStream(SOCKET.getOutputStream());
            var inputStream = new ObjectInputStream(SOCKET.getInputStream());

            receivable.onNewConnection(new DataClient(outputStream, SOCKET));
            new StreamReader(inputStream, receivable).start();

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }

}
