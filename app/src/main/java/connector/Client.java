package connector;

import java.io.*;
import java.net.Socket;
import java.rmi.UnknownHostException;
import java.util.Locale;

public class Client implements OnReceive {
    private Socket SOCKET;

    public void Send(String message) {
        try {
            OutputStream output = SOCKET.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            writer.println(message);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public void onReceive(String message) {
        System.out.println(message);
    }

    public Client(String hostname, OnReceive receivable) {
        try {
            SOCKET = new Socket(hostname, ConnectionSettings.PORT);
            new StreamReader(
                    new BufferedReader(
                            new InputStreamReader(SOCKET.getInputStream())
                    ), receivable).start();
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }

}
