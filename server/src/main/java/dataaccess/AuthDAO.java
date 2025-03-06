package dataaccess;

import DataTransferObjects.RegisterOrLoginResponse;
import DataTransferObjects.RegisterRequest;
import model.AuthData;
import model.UserData;

public interface AuthDAO {
    AuthData getAuthData(String authToken) throws DataAccessException;
    AuthData createAuth(RegisterRequest data) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    void clearAuth() throws DataAccessException;
    String generateToken() throws DataAccessException;
}
