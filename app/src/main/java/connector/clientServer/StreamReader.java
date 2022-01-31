package connector.clientServer;

import connector.protocol.ProtocolMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Class in thread read stream and return object to the event listener
 *
 * @see SocketEventListener
 */

@RequiredArgsConstructor
public class StreamReader extends Thread {
    private final Socket SOCKET;
    private final SocketEventListener SOCKET_EVENT_LISTENER;

    @Override
    @SneakyThrows
    public void run() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(SOCKET.getInputStream());
            while (true) {
                ProtocolMessage message = (ProtocolMessage) objectInputStream.readObject();
                SOCKET_EVENT_LISTENER.onReceiveSocket(message);
                if (message == null) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException ignored) {
            SOCKET.close();
            System.out.println("Disconnect");
        }
    }
}
