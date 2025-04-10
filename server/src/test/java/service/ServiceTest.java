package service;

import data.transfer.objects.GameRequest;
import data.transfer.objects.LoginRequest;
import data.transfer.objects.RegisterOrLoginResponse;
import data.transfer.objects.RegisterRequest;
import chess.ChessGame;
import model.GameData;
import model.GameID;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import shared.ResponseException;
import dataaccess.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ServiceTest {

    UserMemoryDataAccess userDAO;
    AuthMemoryDataAccess authDAO;
    GameMemoryDataAccess gameDAO;
    UserService userService;
    GameService gameService;
    RegisterRequest request;
    RegisterOrLoginResponse response;

    public ServiceTest() throws ResponseException {
        this.userDAO = new UserMemoryDataAccess();
        this.authDAO = new AuthMemoryDataAccess();
        this.gameDAO = new GameMemoryDataAccess();
        this.userService = new UserService(userDAO, authDAO, gameDAO);
        this.gameService = new GameService(userDAO, authDAO, gameDAO);
        this.request = new RegisterRequest("ExistingUser", "ExistingPassword", "ExistingEmail");
        this.response = userService.register(request);
    }

    @Test
    void testGoodRegister() throws ResponseException, DataAccessException {
        HashMap<String, UserData> usersTest = new HashMap<>();
        usersTest.put("NewUser", new UserData("NewUser", "NewPassword", "NewEmail@random"));
        userService.register(new RegisterRequest("NewUser", "NewPassword", "NewEmail@random"));
        Assertions.assertEquals(usersTest.get("NewUser"), userDAO.getUser("NewUser", "NewPassword"));
    }

    @Test
    void testBadRegister() throws ResponseException {
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
            userService.register(new RegisterRequest("ExistingUser", "ExistingPassword", "ExistingEmail"));
        });
        Assertions.assertEquals("Error: already taken", exception.getMessage());
    }

    @Test
    void testGoodLogin() throws ResponseException {
        userService.logout(response.authToken());
        LoginRequest actual = new LoginRequest("ExistingUser", "ExistingPassword");
        Assertions.assertNotNull(userService.login(actual).username());
        Assertions.assertNotNull(userService.login(actual).authToken());
    }

    @Test
    void testBadLogin() throws ResponseException {
        userService.logout(response.authToken());
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
            userService.login(new LoginRequest("NewUser", "NewPassword"));
        });
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void testGoodLogout() throws ResponseException, DataAccessException {
        userService.logout(response.authToken());
        Assertions.assertNull(authDAO.getAuthData(response.authToken()));
    }

    @Test
    void testBadLogout() throws ResponseException {
        userService.clearDatabase();
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
            userService.logout(response.authToken());
        });
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void testClearDatabase() throws ResponseException, DataAccessException {
        gameService.createGame(new GameRequest("NewGame", response.authToken()));
        userService.clearDatabase();
        Assertions.assertNull(userDAO.getUser("ExistingUser", request.password()));
        Assertions.assertNull(authDAO.getAuthData(response.authToken()));
        Assertions.assertNull(gameDAO.getGame(1));
    }

    @Test
    void testGoodListGames() throws ResponseException, DataAccessException {
        gameService.createGame(new GameRequest("Game1", response.authToken()));
        gameService.createGame(new GameRequest("Game2", response.authToken()));
        GameData gameData1 = new GameData(1, null, null, "Game1", new ChessGame());
        GameData gameData2 = new GameData(2, null, null, "Game2", new ChessGame());
        List<GameData> expectedArray = List.of(gameData1, gameData2);
        Collection<GameData> games = gameService.listGames(response.authToken());
        Assertions.assertEquals("Game1", expectedArray.getFirst().gameName());
    }

    @Test
    void testBadListGames() throws ResponseException {
        gameService.createGame(new GameRequest("NewGame", response.authToken()));
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
           gameService.listGames("WrongToken");
        });
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void testGoodCreateGame() throws ResponseException {
        Assertions.assertInstanceOf(
                GameID.class, gameService.createGame(new GameRequest("Game1", response.authToken())));
    }

    @Test
    void testBadCreateGame() {
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
            gameService.createGame(new GameRequest(null, response.authToken()));
        });
        Assertions.assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    void testGoodJoinGame() throws ResponseException, DataAccessException {
        GameID gameID = gameService.createGame(new GameRequest("GoodGame", response.authToken()));
        gameService.joinGame(response.authToken(), ChessGame.TeamColor.WHITE, gameID.gameID());
        Assertions.assertEquals("ExistingUser", gameDAO.getGame(gameID.gameID()).whiteUsername());
    }

    @Test
    void testBadJoinGame() throws ResponseException, DataAccessException{
        RegisterRequest request1 = new RegisterRequest("ExistingUser1", "ExistingPassword1", "ExistingEmail1");
        RegisterOrLoginResponse response1 = userService.register(request1);
        RegisterRequest request2 = new RegisterRequest("ExistingUser2", "ExistingPassword2", "ExistingEmail2");
        RegisterOrLoginResponse response2 = userService.register(request2);
        GameID gameID = gameService.createGame(
                new GameRequest("BadGame", response1.authToken())
        );
        gameService.joinGame(response1.authToken(), ChessGame.TeamColor.BLACK, gameID.gameID());
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
            gameService.joinGame(response2.authToken(), ChessGame.TeamColor.BLACK, gameID.gameID());
        });
        Assertions.assertEquals("Error: Forbidden", exception.getMessage());
    }
}
