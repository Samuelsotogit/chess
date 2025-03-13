package dataaccess;
import data.transfer.objects.RegisterRequest;
import model.UserData;
import java.util.HashMap;

public class UserMemoryDataAccess implements UserDAO {
    private final HashMap<String, UserData> users = new HashMap<>();

    @Override
    public UserData getUser(String username, String password) throws DataAccessException {
        return users.get(username);
    }

    @Override
    public void createUser(RegisterRequest data) throws DataAccessException {
        UserData newUser = new UserData(data.username(), data.password(), data.email());
        users.put(data.username(), newUser);
    }

    @Override
    public void deleteUsers() throws DataAccessException {
        users.clear();
    }
}
