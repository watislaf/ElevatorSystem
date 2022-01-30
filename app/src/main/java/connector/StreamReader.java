package connector;

import connector.protocol.ProtocolMessage;
import lombok.*;

import java.io.*;
import java.net.Socket;

@AllArgsConstructor
public class StreamReader extends Thread {
    private final Socket SOCKET;
    private final OnSocketEvent CALLABLE;

    @Override
    public void run() {
        try {
            System.out.println("CONNECTED");
            ObjectInputStream newStream = new ObjectInputStream(SOCKET.getInputStream());
            while (true) {
                ProtocolMessage message = (ProtocolMessage) newStream.readObject();
                CALLABLE.onReceive(message);
                if (message == null) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            try {
                SOCKET.close();
                System.out.println("DISCONECTED");
                System.out.println(e);
            } catch (IOException ex) {
                System.out.println("CAN'T CLOSE The socket");
            }
///            System.out.println("DISCONECT");
        }
    }
}
