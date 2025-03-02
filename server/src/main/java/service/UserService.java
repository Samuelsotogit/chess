package service;
import dataaccess.DataAccessException;
import dataaccess.UserMemoryDataAccess;
import model.UserData;
import model.AuthData;

public class UserService {

    UserMemoryDataAccess userDAO;

    public UserService(UserMemoryDataAccess userDAO) {
        this.userDAO = userDAO;
    }

    public AuthData register(UserData request) throws DataAccessException {
        // Check if user exists
        if (userDAO.getUser(request.username()) != null) {
            throw new DataAccessException("User already exists");
        }
        userDAO.createUser(request);
        return userDAO.createAuth(request);
    }

    public AuthData login(UserData request) throws DataAccessException {
        AuthData newAuthdata;
        UserData userData = userDAO.getUser(request.username());
        if (userData == null) {
            throw new DataAccessException("User not found");
        } else if (!userData.password().equals(request.password())) {
            throw new DataAccessException("Incorrect password");
        }
        newAuthdata = userDAO.createAuth(userData);
        return newAuthdata;
    }

    public void clearDatabase() throws DataAccessException {
        userDAO.clearAll();
    }
}