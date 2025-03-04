package dataaccess;
import model.AuthData;
import model.UserData;
import model.UserData.*;

public interface UserDAO {
    UserData getUser(String user) throws DataAccessException;
    void createUser(UserData data) throws DataAccessException;
    void deleteUsers();
}
