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
    void GoodCreateUser() throws DataAccessException, ResponseException {
        UserData actualUserData = userDAO.getUser(request.username(), request.password());
        UserData expectedUserData = new UserData(request.username(), request.password(), request.email());
        Assertions.assertEquals(expectedUserData, actualUserData);
    }

    @Test
    void BadCreateUser() throws ResponseException {
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
            userService.register(new RegisterRequest("ExistingUser", "ExistingPassword", "ExistingEmail"));
        });
        Assertions.assertEquals("Error: already taken", exception.getMessage());
    }

    @Test
    void GoodGetUser() throws DataAccessException, ResponseException {
        UserData actualUserData = userDAO.getUser(request.username(), request.password());
        UserData expectedUserData = new UserData(request.username(), request.password(), request.email());
        Assertions.assertEquals(expectedUserData, actualUserData);
    }

    @Test
    void BadGetUser() throws ResponseException {
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
            userService.login(new LoginRequest("ExistingUser", "WrongPassword"));
        });
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void GoodDeleteUsers() throws DataAccessException, ResponseException {
        userDAO.deleteUsers();
        Assertions.assertNull(userDAO.getUser(request.username(), request.password()));
    }

    //Game Tests

    @Test
    void GoodCreateGame() throws DataAccessException, ResponseException {
        GameID id = gameService.createGame(new GameRequest("NewGame", response.authToken()));
        Assertions.assertNotNull(gameDAO.getGame(id.gameID()));
    }

    @Test
    void BadCreateGame() throws ResponseException {
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
            gameService.createGame(new GameRequest("NewGame", "WrongToken"));
        });
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void GoodGetGames() throws DataAccessException, ResponseException {
        gameService.createGame(new GameRequest("NewGame", response.authToken()));
        gameService.createGame(new GameRequest("NewGame2", response.authToken()));
        Assertions.assertEquals(2, gameDAO.getGames().size());
    }

    @Test
    void BadGetGames() throws DataAccessException {
        gameDAO.createGame("NewGame", new ChessGame());
        gameDAO.createGame("NewGame2", new ChessGame());
        gameDAO.deleteGames();
        Collection<GameData> games = gameDAO.getGames();
        Assertions.assertTrue(games.isEmpty());
    }

    @Test
    void GoodGetGame() throws DataAccessException, ResponseException {
        GameID id = gameService.createGame(new GameRequest("NewGame", response.authToken()));
        GameID id2 = gameService.createGame(new GameRequest("NewGame2", response.authToken()));
        GameData gameData1 = gameDAO.getGame(id.gameID());
        GameData gameData2 = gameDAO.getGame(id2.gameID());
        Assertions.assertNotEquals(gameData1, gameData2);
    }

    @Test
    void BadGetGame() throws DataAccessException {
        gameDAO.deleteGames();
        Exception exception = Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.getGame(1);
        });
        Assertions.assertEquals("No games found", exception.getMessage());
    }

    @Test
    void GoodUpdateGame() throws DataAccessException, ResponseException {
        GameID id = gameService.createGame(new GameRequest("NewGame", response.authToken()));
        gameDAO.updateGame(id.gameID(), "White", "Black", "NewGame", new ChessGame());
        GameData gameData = gameDAO.getGame(id.gameID());
        Assertions.assertEquals("NewGame", gameData.gameName());
    }

    @Test
    void BadUpdateGame() throws DataAccessException, ResponseException {
        GameID id = gameService.createGame(new GameRequest("NewGame", response.authToken()));
        gameDAO.updateGame(id.gameID(), "White", "Black", "NewGame", new ChessGame());
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
            gameService.joinGame(response.authToken(), ChessGame.TeamColor.BLACK, id.gameID());
        });
        Assertions.assertEquals("Error: Forbidden", exception.getMessage());
    }

    @Test
    void GoodDeleteGames() throws DataAccessException, ResponseException {
        gameService.createGame(new GameRequest("NewGame", response.authToken()));
        gameService.createGame(new GameRequest("NewGame2", response.authToken()));
        gameDAO.deleteGames();
        Assertions.assertEquals(0, gameDAO.getGames().size());
    }

    // Auth Tests

    @Test
    void GoodCreateAuth() throws DataAccessException, ResponseException {
        AuthData authData = authDAO.createAuth(new RegisterRequest("NewUser", "NewPassword", "NewEmail"));
        Assertions.assertNotNull(authData);
    }

    @Test
    void BadCreateAuth() throws DataAccessException {
        AuthData authData = authDAO.createAuth(new RegisterRequest("NewUser", "NewPassword", "NewEmail"));
        AuthData authData2 = authDAO.createAuth(new RegisterRequest("NewUser2", "NewPassword2", "NewEmail2"));
        Assertions.assertNotSame(authData.authToken(), authData2.authToken());
    }

    @Test
    void GoodGetAuthData() throws DataAccessException, ResponseException {
        AuthData authData = authDAO.getAuthData(response.authToken());
        Assertions.assertNotNull(authData);
    }

    @Test
    void BadGetAuthData() throws DataAccessException {
        AuthData authData = authDAO.getAuthData(response.authToken());
        Assertions.assertNotNull(authData);
        Assertions.assertNull(authDAO.getAuthData("WrongToken"));
    }

    @Test
    void GoodDeleteAuth() throws DataAccessException, ResponseException {
        authDAO.deleteAuth(response.authToken());
        Assertions.assertNull(authDAO.getAuthData(response.authToken()));
    }

    @Test
    void GoodClearAuth() throws DataAccessException, ResponseException {
        authDAO.clearAuth();
        Assertions.assertNull(authDAO.getAuthData(response.authToken()));
    }
}