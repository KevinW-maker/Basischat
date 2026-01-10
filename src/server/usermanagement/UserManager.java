package server.usermanagement;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private final Map<String, String> users = new HashMap<>();

    public UserManager() {
        users.put("alice", "1234");
        users.put("bob", "passwort");
        users.put("admin", "admin");
    }

    public boolean checkCredentials(String username, String password) {
        String storedPassword = users.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }
}