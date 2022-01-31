package connector.clientServer;

import connector.protocol.ProtocolMessage;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;

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

    public void reconect() throws InterruptedException {
        while (true) {
            try {
                serversSocket = new Socket(ConnectionSettings.HOST, ConnectionSettings.PORT);
                objectOutputStream = new ObjectOutputStream(serversSocket.getOutputStream());

                SOCKET_EVENT_LISTENER.onNewSocketConnection(new SocketCompactData(objectOutputStream, serversSocket));
                new StreamReader(serversSocket, SOCKET_EVENT_LISTENER).start();
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
        return serversSocket.isOutputShutdown() ||
                serversSocket.isClosed();
    }
}
