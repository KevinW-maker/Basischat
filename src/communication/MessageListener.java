package communication;

/**
 * Das Observer-Interface.
 * Jede Klasse, die über Netzwerkereignisse (Nachrichten, Status)
 * informiert werden möchte, muss dieses Interface implementieren.
 */
public interface MessageListener {

    /**
     * Wird aufgerufen, wenn eine neue Chat-Nachricht empfangen wird.
     * @param message Der Text der empfangenen Nachricht
     */
    void onMessageReceived(String message);

    /**
     * Wird aufgerufen, um über Statusänderungen zu informieren
     * (z.B. "Verbunden", "Verbindung getrennt").
     * @param status Der Status-Text
     */
    void onStatusUpdate(String status);
}