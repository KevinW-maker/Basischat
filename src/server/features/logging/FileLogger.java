package server.features.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class FileLogger implements IChatLogger {
    @Override
    public void log(String message) {
        try (PrintWriter out = new PrintWriter(new FileWriter("chat_log.txt", true))) {
            out.println(new Date() + ": " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}