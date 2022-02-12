package connector.clientServer;

import connector.protocol.ProtocolMessage;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * This class can connect to the Server,
 * get all connection information from Connection Settings class
 *
 * @see ConnectionSettings
 * @see Server
 */

@RequiredArgsConstructor
public class Client {
    private final SocketEventListener SOCKET_EVENT_LISTENER;
    private ObjectOutputStream objectOutputStream;
    private Socket serversSocket;
    private StreamReader streamReader;

    public void reconect() throws InterruptedException {
        while (true) {
            try {
                serversSocket = new Socket(ConnectionSettings.HOST, ConnectionSettings.PORT);
                objectOutputStream = new ObjectOutputStream(serversSocket.getOutputStream());

                SOCKET_EVENT_LISTENER.onNewSocketConnection(new SocketCompactData(objectOutputStream, serversSocket));
                streamReader = new StreamReader(serversSocket, SOCKET_EVENT_LISTENER);
                streamReader.start();
                return;
            } catch (IOException ignored) {
            }
            TimeUnit.MILLISECONDS.sleep(400);
        }
    }

    public void send(ProtocolMessage message) {
        try {
            objectOutputStream.writeObject(message);
        } catch (IOException ignored) {
        }
    }

    public boolean isClosed() {
        if (serversSocket == null) {
            return true;
        }
        return !streamReader.isAlive();
    }
}
