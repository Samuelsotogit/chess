package dataaccess;

import model.AuthData;
import model.UserData;
import java.util.HashMap;
import java.util.UUID;

public class AuthMemoryDataAccess implements AuthDAO {
    private final HashMap<String, AuthData> authTokens = new HashMap<>();

    @Override
    public AuthData createAuth(UserData data) throws DataAccessException {
        if (data == null || data.username() == null) {
            System.out.println("User not found");
            throw new DataAccessException("User not found");
        }
        String newToken = generateToken();
        AuthData newAuthData = new AuthData(data.username(), newToken);
        authTokens.put(newToken, newAuthData);
        return newAuthData;
    }

    @Override
    public AuthData getAuthData(String authToken) {
        // I am supposed to find the user by the authToken. Fix this.
        return authTokens.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        authTokens.remove(authToken);
    }

    @Override
    public void clearAuth() {
        authTokens.clear();
    }

    @Override
    public String generateToken() {
        return UUID.randomUUID().toString();
    }
}
