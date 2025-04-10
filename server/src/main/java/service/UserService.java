package service;
import data.transfer.objects.LoginRequest;
import data.transfer.objects.RegisterOrLoginResponse;
import data.transfer.objects.RegisterRequest;
import dataaccess.*;
import shared.ResponseException;
import model.UserData;
import model.AuthData;

public class UserService {

    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public RegisterOrLoginResponse register(RegisterRequest request) throws ResponseException {
        // Check if user exists
        try {
            UserData user = userDAO.getUser(request.username(), request.password());
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
            UserData userData = userDAO.getUser(request.username(), request.password());
            if (userData == null) {
                throw new ResponseException(401, "Error: unauthorized");
            } else if (!userData.password().equals(request.password())) {
                throw new ResponseException(401, "Error: unauthorized");
            }
            newAuthdata = authDAO.createAuth(new RegisterRequest(userData.username(), userData.password(), userData.email()));
            return new RegisterOrLoginResponse(newAuthdata.username(), newAuthdata.authToken());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
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
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Error: Internal Server Error");
        }
    }
}