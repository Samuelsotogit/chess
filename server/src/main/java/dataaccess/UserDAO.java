package dataaccess;

import data.transfer.objects.RegisterRequest;
import model.UserData;
import server.ResponseException;

public interface UserDAO {
    UserData getUser(String user, String password) throws DataAccessException, ResponseException;
    void createUser(RegisterRequest data) throws DataAccessException;
    void deleteUsers() throws DataAccessException;
}
