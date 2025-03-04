package service;
import dataaccess.AuthMemoryDataAccess;
import dataaccess.DataAccessException;
import dataaccess.UserMemoryDataAccess;
import model.UserData;
import model.AuthData;

public class UserService {

    UserMemoryDataAccess userDAO;
    AuthMemoryDataAccess authDAO;

    public UserService(UserMemoryDataAccess userDAO, AuthMemoryDataAccess authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(UserData request) throws DataAccessException {
        // Check if user exists
        if (userDAO.getUser(request.username()) != null) {
            throw new DataAccessException("User already exists");
        } else if (request.password() == null) {
            throw new DataAccessException("Password field is empty");
        }
        userDAO.createUser(request);
        return authDAO.createAuth(request);
    }

    public AuthData login(UserData request) throws DataAccessException {
        AuthData newAuthdata;
        UserData userData = userDAO.getUser(request.username());
        if (userData == null) {
            throw new DataAccessException("User not found");
        } else if (!userData.password().equals(request.password())) {
            throw new DataAccessException("Incorrect password");
        }
        newAuthdata = authDAO.createAuth(userData);
        return newAuthdata;
    }

    public void logout(String authToken) throws DataAccessException {
        AuthData authData = authDAO.getAuthData(authToken);
        if (authData == null) {
            throw new DataAccessException("unauthorized");
        }
        authDAO.deleteAuth(authToken);
    }

    public void clearDatabase() throws DataAccessException {
        userDAO.deleteUsers();
        authDAO.clearAuth();
    }
}