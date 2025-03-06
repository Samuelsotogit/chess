package service;
import DataTransferObjects.LoginRequest;
import DataTransferObjects.RegisterOrLoginResponse;
import DataTransferObjects.RegisterRequest;
import dataaccess.AuthMemoryDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameMemoryDataAccess;
import server.ResponseException;
import dataaccess.UserMemoryDataAccess;
import model.UserData;
import model.AuthData;

public class UserService {

    UserMemoryDataAccess userDAO;
    AuthMemoryDataAccess authDAO;
    GameMemoryDataAccess gameDAO;

    public UserService(UserMemoryDataAccess userDAO, AuthMemoryDataAccess authDAO, GameMemoryDataAccess gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public RegisterOrLoginResponse register(RegisterRequest request) throws ResponseException {
        // Check if user exists
        try {
            UserData user = userDAO.getUser(request.username());
            if (user != null) {
                throw new ResponseException(403, "Error: already taken");
            }
            userDAO.createUser(request);
            AuthData authData = authDAO.createAuth(request);
            return new RegisterOrLoginResponse(authData.username(), authData.authToken());
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Error: Internal Server Error");
        }
    }

    public RegisterOrLoginResponse login(LoginRequest request) throws ResponseException {
        try {
            AuthData newAuthdata;

            UserData userData = userDAO.getUser(request.username());
            if (userData == null) {
                throw new ResponseException(401, "Error: unauthorized");
            } else if (!userData.password().equals(request.password())) {
                throw new ResponseException(401, "Error: unauthorized");
            }
            newAuthdata = authDAO.createAuth(new RegisterRequest(userData.username(), userData.password(), userData.email()));
            return new RegisterOrLoginResponse(newAuthdata.username(), newAuthdata.authToken());
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Error: Internal Server Error");
        }
    }

    public void logout(String authToken) throws ResponseException {
        try {
            AuthData authData = authDAO.getAuthData(authToken);
            if (authData == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }
            authDAO.deleteAuth(authToken);
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Error: Internal Server Error");
        }
    }

    public void clearDatabase() throws ResponseException {
        try {
            userDAO.deleteUsers();
            authDAO.clearAuth();
            gameDAO.deleteGames();
            if (!userDAO.getUsers().isEmpty() || !authDAO.getAuthTokens().isEmpty() || !gameDAO.getGames().isEmpty()) {
                throw new ResponseException(401, "Error: Unauthorized");
            }
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Error: Internal Server Error");
        }
    }
}