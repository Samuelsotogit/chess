package dataaccess;

import data.transfer.objects.RegisterRequest;
import model.UserData;
import server.ResponseException;

import java.util.HashMap;

public interface UserDAO {
    UserData getUser(String user) throws DataAccessException, ResponseException;
    void createUser(RegisterRequest data) throws DataAccessException;
    void deleteUsers() throws DataAccessException;
//    HashMap<String, UserData> getUsers() throws DataAccessException;
}
