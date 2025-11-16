package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class TcpNetworkService implements NetworkService {

    private final List<MessageListener> listeners = new ArrayList<>();

    private Socket socket;
    private ServerSocket serverSocket;
    private PrintWriter out;
    private BufferedReader in;

    @Override
    public void startListening(int port) throws IOException {
        if (socket != null || serverSocket != null) {
            notifyStatus("Bereits verbunden oder lauschend. Bitte zuerst trennen.");
            return;
        }

        serverSocket = new ServerSocket(port);
        notifyStatus("Lausche auf Port " + port + "...");

        new Thread(() -> {
            try {
                Socket clientSocket = serverSocket.accept();
                setupConnection(clientSocket);
            } catch (IOException e) {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    notifyStatus("Fehler beim Warten auf Verbindung: " + e.getMessage());
                }
            }
        }).start();
    }

    @Override
    public void connect(String ip, int port) throws IOException {
        if (socket != null || serverSocket != null) {
            notifyStatus("Bereits verbunden oder lauschend. Bitte zuerst trennen.");
            return;
        }

        Socket clientSocket = new Socket(ip, port);
        setupConnection(clientSocket);
    }

    private void setupConnection(Socket s) throws IOException {
        this.socket = s;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        notifyStatus("Verbunden mit " + socket.getInetAddress().getHostAddress());

        startReceiveLoop();
    }

    private void startReceiveLoop() {
        new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    notifyMessageReceived(line); // Benachrichtige alle Listener
                }
            } catch (IOException e) {
                if (socket != null && !socket.isClosed()) {
                    notifyStatus("Verbindung verloren.");
                }
            } finally {
                disconnect();
            }
        }).start();
    }

    @Override
    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        } else {
            notifyStatus("Keine Verbindung. Nachricht konnte nicht gesendet werden.");
        }
    }

    @Override
    public void disconnect() {
        notifyStatus("Verbindung wird getrennt...");
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            notifyStatus("Fehler beim Trennen: " + e.getMessage());
        } finally {
            socket = null;
            serverSocket = null;
            in = null;
            out = null;
            notifyStatus("Verbindung getrennt.");
        }
    }


    @Override
    public void addMessageListener(MessageListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeMessageListener(MessageListener listener) {
        listeners.remove(listener);
    }

    private void notifyMessageReceived(String message) {
        for (MessageListener listener : new ArrayList<>(listeners)) {
            listener.onMessageReceived(message);
        }
    }

    private void notifyStatus(String status) {
        for (MessageListener listener : new ArrayList<>(listeners)) {
            listener.onStatusUpdate(">> " + status);
        }
    }
}