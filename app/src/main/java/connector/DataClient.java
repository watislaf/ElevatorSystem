package connector;

import lombok.AllArgsConstructor;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

@AllArgsConstructor
public class DataClient {
    final ObjectOutputStream STREAM;
    final Socket SOCKET;

    public boolean isClosed() {
        return SOCKET.isClosed();
    }
}

