package server;
import dataaccess.AuthMemoryDataAccess;
import dataaccess.UserMemoryDataAccess;
import server.handlers.DeleteHandler;
import server.handlers.LoginHandler;
import server.handlers.RegistrationHandler;
import service.UserService;
import spark.*;

public class Server {

    UserMemoryDataAccess userDAO = new UserMemoryDataAccess();
    AuthMemoryDataAccess authDAO = new AuthMemoryDataAccess();
    UserService userService;
    LoginHandler loginHandler;
    RegistrationHandler registrationHandler;
    DeleteHandler deleteHandler;

    public Server() {
        this.userService = new UserService(userDAO, authDAO);
        this.loginHandler = new LoginHandler(userService);
        this.registrationHandler = new RegistrationHandler(userService);
        this.deleteHandler = new DeleteHandler(userService);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", registrationHandler::registerUser);
        Spark.delete("/db", deleteHandler::clearDatabase);
        Spark.post("/session", loginHandler::loginUser);
        Spark.delete("session", loginHandler::logout);
//        Spark.get("/game", );
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}
