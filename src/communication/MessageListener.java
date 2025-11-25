package communication;


public interface MessageListener {


    void onMessageReceived(String message);


    void onStatusUpdate(String status);
}