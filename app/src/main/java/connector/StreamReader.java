package connector;

import java.io.*;
import java.util.Arrays;

public class StreamReader extends Thread {
    private final BufferedReader READER;
    private final OnReceive CALLABLE;

    StreamReader(InputStream input, OnReceive callable) {
        this.READER = new BufferedReader(new InputStreamReader(input));
        this.CALLABLE = callable;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String message = READER.readLine();
                CALLABLE.onReceive(message);
                if (message == null) {
                    break;
                }
            } catch (IOException e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }
    }
}
