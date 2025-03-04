package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    AuthData getAuthData(String authToken) throws DataAccessException;
    AuthData createAuth(UserData data) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    void clearAuth();
    String generateToken() throws DataAccessException;
}
