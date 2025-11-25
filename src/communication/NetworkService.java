package communication;

import java.io.IOException;

/**
 * Definiert den Vertrag für den Netzwerkdienst.
 * Dies ist die Schnittstelle, die von der UI-Schicht verwendet wird.
 */
public interface NetworkService {

    /**
     * Beginnt, auf eingehende Verbindungen auf dem angegebenen Port zu lauschen.
     * @param port Der Port, auf dem gelauscht werden soll
     * @throws IOException Wenn der Port bereits belegt ist
     */
    void startListening(int port) throws IOException;

    /**
     * Versucht, eine Verbindung zu einem Server herzustellen.
     * @param ip Die IP-Adresse des Servers
     * @param port Der Port des Servers
     * @throws IOException Wenn die Verbindung fehlschlägt
     */
    void connect(String ip, int port) throws IOException;

    /**
     * Sendet eine Nachricht über die bestehende Verbindung.
     * @param message Die zu sendende Nachricht
     */
    void sendMessage(String message);


    void disconnect();


    void addMessageListener(MessageListener listener);


    void removeMessageListener(MessageListener listener);
}