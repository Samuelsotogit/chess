package client;

import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import shared.ResponseException;
import model.AuthData;
import model.GameData;
import model.GameID;

import java.util.Collection;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    public static void stopServer() throws ResponseException {
        facade.clear();
        server.stop();
    }

    @BeforeEach
    public void clearDatabase() throws Exception {
        facade.clear();
    }

    @Test
    public void testRegisterPositive() throws Exception {
        AuthData authData = facade.register("player1", "password", "player1@email.com");
        Assertions.assertNotNull(authData, "AuthData should not be null");
    }

    @Test
    public void testRegisterNegative() {
        Assertions.assertThrows(Exception.class, () -> {
            facade.register(null, null, null);
        }, "Registering with invalid data should throw an exception");
    }

    @Test
    public void testLoginPositive() throws Exception {
        facade.register("player2", "password", "player2@email.com");
        AuthData authData = facade.login("player2", "password");
        Assertions.assertNotNull(authData, "AuthData should not be null");
    }

    @Test
    public void testLoginNegative() {
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.login("nonexistent", "wrongPassword");
        }, "Logging in with invalid credentials should throw a ResponseException");
    }

    @Test
    public void testCreateGamePositive() throws Exception {
        AuthData authData = facade.register("player3", "password", "player3@email.com");
        GameID gameID = facade.createGame("TestGame", authData.authToken());
        Assertions.assertNotNull(gameID, "GameID should not be null");
    }

    @Test
    public void testCreateGameNegative() {
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.createGame("TestGame", "invalidToken");
        }, "Creating a game with an invalid token should throw a ResponseException");
    }

    @Test
    public void testListGamesPositive() throws Exception {
        AuthData authData = facade.register("player4", "password", "player4@email.com");
        facade.createGame("Game1", authData.authToken());
        Collection<GameData> games = facade.listGames(authData.username(), authData.authToken());
        Assertions.assertNotNull(games, "Games list should not be null");
        Assertions.assertFalse(games.isEmpty(), "Games list should not be empty");
    }

    @Test
    public void testListGamesNegative() {
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.listGames("invalidUser", "invalidToken");
        }, "Listing games with invalid credentials should throw a ResponseException");
    }

    @Test
    public void testJoinGamePositive() throws Exception {
        AuthData authData = facade.register("player5", "password", "player5@email.com");
        GameID gameID = facade.createGame("GameToJoin", authData.authToken());
        facade.joinGame(authData.authToken(), "white", String.valueOf(gameID.gameID()));
    }

    @Test
    public void testJoinGameNegative() {
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.joinGame("invalidToken", "white", "invalidGameID");
        }, "Joining a game with invalid data should throw a ResponseException");
    }
}