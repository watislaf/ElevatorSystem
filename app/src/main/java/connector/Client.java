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
        return socket.isClosed() || !socket.isConnected();
    }

    public void send(ProtocolMessage message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void connect(String hostname, OnSocketEvent receivable) {
        while (true) {
            try {
                socket = new Socket(hostname, ConnectionSettings.PORT);
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                var inputStream = new ObjectInputStream(socket.getInputStream());

                receivable.onNewConnection(new DataClient(outputStream, socket));
                new StreamReader(socket,inputStream, receivable).start();
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
