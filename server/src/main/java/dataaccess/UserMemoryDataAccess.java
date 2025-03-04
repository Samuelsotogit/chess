package dataaccess;
import model.AuthData;
import model.UserData;
import java.util.HashMap;
import java.util.UUID;

public class UserMemoryDataAccess implements UserDAO {
    private final HashMap<String, UserData> users = new HashMap<>();

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if (users.containsKey(username)) {
            return users.get(username);
        }
        return null;
    }

    @Override
    public void createUser(UserData data) throws DataAccessException {
        UserData newUser = new UserData(data.username(), data.password(), data.email());
        users.put(data.username(), newUser);
    }

    @Override
    public void deleteUsers() {
        users.clear();
    }
}
