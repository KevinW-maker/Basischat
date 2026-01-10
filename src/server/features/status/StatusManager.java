package server.features.status;

import java.util.HashMap;
import java.util.Map;

public class StatusManager {
    private final Map<String, String> userStatus = new HashMap<>();

    public void setStatus(String username, String status) {
        userStatus.put(username, status);
    }

    public String getStatus(String username) {
        return userStatus.getOrDefault(username, "Online");
    }
}