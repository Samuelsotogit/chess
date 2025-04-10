package dataaccess;

import data.transfer.objects.RegisterRequest;
import model.AuthData;

public interface AuthDAO {
    AuthData getAuthData(String authToken) throws DataAccessException;
    AuthData createAuth(RegisterRequest data) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    void clearAuth() throws DataAccessException;
    String generateToken() throws DataAccessException;
}
