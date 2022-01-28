package connector;

import connector.protocol.ProtocolMessage;
import lombok.*;

import java.io.*;

@AllArgsConstructor
public class StreamReader extends Thread {
    private final ObjectInputStream READER;
    private final OnSocketEvent CALLABLE;

    @Override
    public void run() {
        try {
            while (true) {
                ProtocolMessage message = (ProtocolMessage) READER.readObject();
                CALLABLE.onReceive(message);
                if (message == null) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }
    }
}
