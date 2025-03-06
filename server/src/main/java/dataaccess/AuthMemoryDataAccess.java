package dataaccess;

import DataTransferObjects.RegisterRequest;
import model.AuthData;
import java.util.HashMap;
import java.util.UUID;

public class AuthMemoryDataAccess implements AuthDAO {
    private final HashMap<String, AuthData> authTokens = new HashMap<>();

    @Override
    public AuthData createAuth(RegisterRequest data) throws DataAccessException {
        String newToken = generateToken();
        AuthData newAuthData = new AuthData(data.username(), newToken);
        authTokens.put(newToken, newAuthData);
        return newAuthData;
    }

    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException {
        return authTokens.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        authTokens.remove(authToken);
    }

    @Override
    public void clearAuth() throws DataAccessException {
        authTokens.clear();
    }

    @Override
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public HashMap<String, AuthData> getAuthTokens() {
        return authTokens;
    }
}
