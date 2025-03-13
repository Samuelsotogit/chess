package dataaccess;

import data.transfer.objects.RegisterRequest;
import model.AuthData;
import model.UserData;
import server.ResponseException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class AuthMySqlDataAccess implements AuthDAO {

    DatabaseManager databaseManager = new DatabaseManager();

    public AuthMySqlDataAccess() throws DataAccessException {
            String[] createGameTable = {
                    """
            CREATE TABLE IF NOT EXISTS  authtable (
              `username` VARCHAR(100),
              `authToken` VARCHAR(100) NOT NULL UNIQUE,
              PRIMARY KEY (`authToken`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
            };
            databaseManager.configureDatabase(createGameTable);
    }

    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username FROM authtable WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        var username = rs.getString("username");
                        return new AuthData(username, authToken);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public AuthData createAuth(RegisterRequest data) throws DataAccessException {
        var statement = "INSERT INTO authtable (username, authToken) VALUES (?, ?)";
            String authToken = generateToken();
            databaseManager.executeUpdate(statement, data.username(), authToken);
        return new AuthData(data.username(), authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM authtable WHERE authToken=?";
        databaseManager.executeUpdate(statement, authToken);
    }

    @Override
    public void clearAuth() throws DataAccessException {
        var statement = "TRUNCATE authtable";
        databaseManager.executeUpdate(statement);
    }

    @Override
    public String generateToken() {
        return UUID.randomUUID().toString();
    }
}
