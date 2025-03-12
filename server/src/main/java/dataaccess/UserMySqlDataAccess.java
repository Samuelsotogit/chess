package dataaccess;

import data.transfer.objects.RegisterRequest;
import model.UserData;
import server.ResponseException;
import java.util.HashMap;

public class UserMySqlDataAccess implements UserDAO {

    DatabaseManager databaseManager = new DatabaseManager();

    public UserMySqlDataAccess() {
        try {
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
        } catch (ResponseException | DataAccessException exception) {
            System.out.println("Data Access Error");
        }
    }

    @Override
    public UserData getUser(String user) throws DataAccessException {
        return null;
    }

    @Override
    public void createUser(RegisterRequest data) throws DataAccessException {

    }

    @Override
    public void deleteUsers() throws DataAccessException {

    }

    @Override
    public HashMap<String, UserData> getUsers() {
        return null;
    }
}
