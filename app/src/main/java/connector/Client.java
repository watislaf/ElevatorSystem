package connector;

import connector.protocol.ProtocolMessage;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Client {
    private ObjectOutputStream outputStream;
    private Socket socket;

    public boolean isClosed() {
        if (socket == null) {
            return true;
        }
        return !socket.isConnected()||socket.isClosed()||socket.isOutputShutdown();
    }

    public void send(ProtocolMessage message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void connect(String hostname, OnSocketEvent receivable) {
        if (!isClosed()) {
            return;
        }
        while (true) {
            try {
                socket = new Socket(hostname, ConnectionSettings.PORT);
                outputStream = new ObjectOutputStream(socket.getOutputStream());

                receivable.onNewConnection(new DataClient(outputStream, socket));
                new StreamReader(socket, socket.getInputStream(), receivable).start();
                return;
            } catch (IOException e) {
                System.out.println(e);
            }
            try {
                TimeUnit.MILLISECONDS.sleep(Math.round(400.));
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

}
