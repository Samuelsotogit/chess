package dataaccess;

import chess.ChessGame;
import data.transfer.objects.GameRequest;
import data.transfer.objects.LoginRequest;
import data.transfer.objects.RegisterOrLoginResponse;
import data.transfer.objects.RegisterRequest;
import model.AuthData;
import model.GameData;
import model.GameID;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.ResponseException;
import service.GameService;
import service.UserService;

import java.util.Collection;

public class DataAccessTests {
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;
    UserService userService;
    GameService gameService;
    RegisterRequest request;
    RegisterOrLoginResponse response;

    @BeforeEach
    public void setUp() throws DataAccessException, ResponseException {
        this.userDAO = new UserMySqlDataAccess();
        this.authDAO = new AuthMySqlDataAccess();
        this.gameDAO = new GameMySqlDataAccess();
        this.userService = new UserService(userDAO, authDAO, gameDAO);
        this.gameService = new GameService(userDAO, authDAO, gameDAO);
        this.request = new RegisterRequest("ExistingUser", "ExistingPassword", "ExistingEmail");
        userDAO.deleteUsers();
        authDAO.clearAuth();
        gameDAO.deleteGames();
        this.response = userService.register(request);
    }

    //User Tests

    @Test
    void PositiveCreateUser() throws DataAccessException, ResponseException {
        UserData actualUserData = userDAO.getUser(request.username(), request.password());
        UserData expectedUserData = new UserData(request.username(), request.password(), request.email());
        Assertions.assertEquals(expectedUserData, actualUserData);
    }

    @Test
    void NegativeCreateUser() throws ResponseException {
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
            userService.register(new RegisterRequest("ExistingUser", "ExistingPassword", "ExistingEmail"));
        });
        Assertions.assertEquals("Error: already taken", exception.getMessage());
    }

    @Test
    void PositiveGetUser() throws DataAccessException, ResponseException {
        UserData actualUserData = userDAO.getUser(request.username(), request.password());
        UserData expectedUserData = new UserData(request.username(), request.password(), request.email());
        Assertions.assertEquals(expectedUserData, actualUserData);
    }

    @Test
    void NegativeGetUser() throws ResponseException {
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
            userService.login(new LoginRequest("ExistingUser", "WrongPassword"));
        });
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void PositiveDeleteUsers() throws DataAccessException, ResponseException {
        userDAO.deleteUsers();
        Assertions.assertNull(userDAO.getUser(request.username(), request.password()));
    }

    //Game Tests

    @Test
    void PositiveCreateGame() throws DataAccessException, ResponseException {
        GameID id = gameService.createGame(new GameRequest("NewGame", response.authToken()));
        Assertions.assertNotNull(gameDAO.getGame(id.gameID()));
    }

    @Test
    void NegativeCreateGame() throws ResponseException {
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
            gameService.createGame(new GameRequest("NewGame", "WrongToken"));
        });
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void PositiveGetGames() throws DataAccessException, ResponseException {
        gameService.createGame(new GameRequest("NewGame", response.authToken()));
        gameService.createGame(new GameRequest("NewGame2", response.authToken()));
        Assertions.assertEquals(2, gameDAO.getGames().size());
    }

    @Test
    void NegativeGetGames() throws DataAccessException {
        gameDAO.createGame("NewGame", new ChessGame());
        gameDAO.createGame("NewGame2", new ChessGame());
        gameDAO.deleteGames();
        Collection<GameData> games = gameDAO.getGames();
        Assertions.assertTrue(games.isEmpty());
    }

    @Test
    void PositiveGetGame() throws DataAccessException, ResponseException {
        GameID id = gameService.createGame(new GameRequest("NewGame", response.authToken()));
        GameID id2 = gameService.createGame(new GameRequest("NewGame2", response.authToken()));
        GameData gameData1 = gameDAO.getGame(id.gameID());
        GameData gameData2 = gameDAO.getGame(id2.gameID());
        Assertions.assertNotEquals(gameData1, gameData2);
    }

    @Test
    void NegativeGetGame() throws DataAccessException {
        gameDAO.deleteGames();
        Exception exception = Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.getGame(1);
        });
        Assertions.assertEquals("No games found", exception.getMessage());
    }

    @Test
    void PositiveUpdateGame() throws DataAccessException, ResponseException {
        GameID id = gameService.createGame(new GameRequest("NewGame", response.authToken()));
        gameDAO.updateGame(id.gameID(), "White", "Black", "NewGame", new ChessGame());
        GameData gameData = gameDAO.getGame(id.gameID());
        Assertions.assertEquals("NewGame", gameData.gameName());
    }

    @Test
    void NegativeUpdateGame() throws DataAccessException, ResponseException {
        GameID id = gameService.createGame(new GameRequest("NewGame", response.authToken()));
        gameDAO.updateGame(id.gameID(), "White", "Black", "NewGame", new ChessGame());
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
            gameService.joinGame(response.authToken(), ChessGame.TeamColor.BLACK, id.gameID());
        });
        Assertions.assertEquals("Error: Forbidden", exception.getMessage());
    }

    @Test
    void PositiveDeleteGames() throws DataAccessException, ResponseException {
        gameService.createGame(new GameRequest("NewGame", response.authToken()));
        gameService.createGame(new GameRequest("NewGame2", response.authToken()));
        gameDAO.deleteGames();
        Assertions.assertEquals(0, gameDAO.getGames().size());
    }

    // Auth Tests

    @Test
    void PositiveCreateAuth() throws DataAccessException, ResponseException {
        AuthData authData = authDAO.createAuth(new RegisterRequest("NewUser", "NewPassword", "NewEmail"));
        Assertions.assertNotNull(authData);
    }

    @Test
    void NegativeCreateAuth() throws DataAccessException {
        AuthData authData = authDAO.createAuth(new RegisterRequest("NewUser", "NewPassword", "NewEmail"));
        AuthData authData2 = authDAO.createAuth(new RegisterRequest("NewUser2", "NewPassword2", "NewEmail2"));
        Assertions.assertNotSame(authData.authToken(), authData2.authToken());
    }

    @Test
    void PositiveGetAuthData() throws DataAccessException, ResponseException {
        AuthData authData = authDAO.getAuthData(response.authToken());
        Assertions.assertNotNull(authData);
    }

    @Test
    void NegativeGetAuthData() throws DataAccessException {
        AuthData authData = authDAO.getAuthData(response.authToken());
        Assertions.assertNotNull(authData);
        Assertions.assertNull(authDAO.getAuthData("WrongToken"));
    }

    @Test
    void PositiveDeleteAuth() throws DataAccessException, ResponseException {
        authDAO.deleteAuth(response.authToken());
        Assertions.assertNull(authDAO.getAuthData(response.authToken()));
    }

    @Test
    void PositiveClearAuth() throws DataAccessException, ResponseException {
        authDAO.clearAuth();
        Assertions.assertNull(authDAO.getAuthData(response.authToken()));
    }
}