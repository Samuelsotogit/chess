package dataaccess;

import data.transfer.objects.RegisterRequest;
import model.AuthData;

import java.util.HashMap;

public class AuthMySqlDataAccess implements AuthDAO {
    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData createAuth(RegisterRequest data) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public void clearAuth() throws DataAccessException {

    }

    @Override
    public String generateToken() throws DataAccessException {
        return "";
    }

    @Override
    public HashMap<String, AuthData> getAuthTokens() throws DataAccessException {
        return null;
    }
}
