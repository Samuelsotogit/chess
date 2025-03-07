package dataaccess;

import data.transfer.objects.RegisterRequest;
import model.UserData;

public interface UserDAO {
    UserData getUser(String user) throws DataAccessException;
    void createUser(RegisterRequest data) throws DataAccessException;
    void deleteUsers() throws DataAccessException;
}
