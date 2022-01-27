package connector;

import lombok.AllArgsConstructor;

import java.io.*;
import java.util.Arrays;

@AllArgsConstructor
public class StreamReader extends Thread {
    private final BufferedReader READER;
    private final OnReceive CALLABLE;
    
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
