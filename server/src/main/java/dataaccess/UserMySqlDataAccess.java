package dataaccess;

import data.transfer.objects.RegisterRequest;
import model.GameData;
import model.UserData;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;
import server.ResponseException;

public class UserMySqlDataAccess implements UserDAO {

    DatabaseManager databaseManager = new DatabaseManager();

    public UserMySqlDataAccess() throws DataAccessException {
            String[] createUserTable = {
                    """
            CREATE TABLE IF NOT EXISTS  users (
              `username` VARCHAR(50) NOT NULL UNIQUE,
              `password` VARCHAR(100) NOT NULL,
              `email` VARCHAR(50) NOT NULL,
              PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
            };
            databaseManager.configureDatabase(createUserTable);
    }

    @Override
    public UserData getUser(String user, String password) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM users WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, user);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        var username = rs.getString("username");
                        var hashedPassword = rs.getString("password");
                        var email = rs.getString("email");
                        return validatePassword(username, password, email, hashedPassword);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void createUser(RegisterRequest data) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        String hashedPassword = BCrypt.hashpw(data.password(), BCrypt.gensalt());
        databaseManager.executeUpdate(statement, data.username(), hashedPassword, data.email());
    }

    @Override
    public void deleteUsers() throws DataAccessException {
        var statement = "TRUNCATE users";
        databaseManager.executeUpdate(statement);
    }

    UserData validatePassword(String username, String password, String email, String hashedPassword) {
        if (BCrypt.checkpw(password, hashedPassword)) {
            return new UserData(username, password, email);
        } return null;
    }
}
