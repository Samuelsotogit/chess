package dataaccess;

import data.transfer.objects.RegisterRequest;
import model.AuthData;
import server.ResponseException;

import java.util.HashMap;

public class AuthMySqlDataAccess implements AuthDAO {

    DatabaseManager databaseManager = new DatabaseManager();

    public AuthMySqlDataAccess() {
        try {
            String[] createGameTable = {
                    """
            CREATE TABLE IF NOT EXISTS  AuthTable (
              `username` VARCHAR(100),
              `authToken` VARCHAR(100) NOT NULL UNIQUE,
              PRIMARY KEY (`authToken`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
            };
            databaseManager.configureDatabase(createGameTable);
        } catch (ResponseException | DataAccessException exception) {
            System.out.println("Data Access Error");
        }
    }

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
