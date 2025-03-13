package server;
import dataaccess.*;
import server.handlers.DeleteHandler;
import server.handlers.LoginHandler;
import server.handlers.RegistrationHandler;
import server.handlers.GamesHandler;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    UserService userService;
    GameService gameService;
    LoginHandler loginHandler;
    RegistrationHandler registrationHandler;
    DeleteHandler deleteHandler;
    GamesHandler gamesHandler;

    public Server() {
        try {
//          UserDAO userDAO = new UserMemoryDataAccess();
//          AuthDAO authDAO = new AuthMemoryDataAccess();
//          GameDAO gameDAO = new GameMemoryDataAccess();
            UserDAO userDAO = new UserMySqlDataAccess();
            AuthDAO authDAO = new AuthMySqlDataAccess();
            GameDAO gameDAO = new GameMySqlDataAccess();
            this.userService = new UserService(userDAO, authDAO, gameDAO);
            this.gameService = new GameService(userDAO, authDAO, gameDAO);
            this.loginHandler = new LoginHandler(userService);
            this.registrationHandler = new RegistrationHandler(userService);
            this.deleteHandler = new DeleteHandler(userService);
            this.gamesHandler = new GamesHandler(gameService);
        } catch (Throwable e) {
            System.out.println("Error initializing server");
        }
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", registrationHandler::registerUser);
        Spark.delete("/db", deleteHandler::clearDatabase);
        Spark.post("/session", loginHandler::loginUser);
        Spark.delete("session", loginHandler::logout);
        Spark.post("/game", gamesHandler::createGame);
        Spark.put("/game", gamesHandler::joinGame);
        Spark.get("/game", gamesHandler::listGames);
        Spark.exception(ResponseException.class, this::exceptionHandler);
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.statusCode());
        res.body(ex.toJson());
    }
}
