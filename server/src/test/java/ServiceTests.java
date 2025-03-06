import DataTransferObjects.RegisterRequest;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import server.ResponseException;
import service.*;
import dataaccess.*;

import java.util.HashMap;

public class ServiceTests {

    UserMemoryDataAccess userDAO;
    AuthMemoryDataAccess authDAO;
    GameMemoryDataAccess gameDAO;
    UserService userService;
    GameService gameService;

    public ServiceTests() {
        this.userDAO = new UserMemoryDataAccess();
        this.authDAO = new AuthMemoryDataAccess();
        this.gameDAO = new GameMemoryDataAccess();
        this.userService = new UserService(userDAO, authDAO, gameDAO);
        this.gameService = new GameService(userDAO, authDAO, gameDAO);
    }

    @Test
    void testGoodRegister() throws ResponseException, DataAccessException {
        HashMap<String, UserData> usersTest = new HashMap<>();
        usersTest.put("NewUser", new UserData("NewUser", "NewPassword", "NewEmail@random"));
        userService.register(new RegisterRequest("NewUser", "NewPassword", "NewEmail@random"));
        Assertions.assertEquals(usersTest.get("NewUser"), userDAO.getUser("NewUser"));
    }

}
