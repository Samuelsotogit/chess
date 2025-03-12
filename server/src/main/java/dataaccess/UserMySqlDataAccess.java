package dataaccess;

import data.transfer.objects.RegisterRequest;
import model.UserData;

import java.awt.geom.NoninvertibleTransformException;
import java.security.PublicKey;
import java.util.HashMap;

public class UserMySqlDataAccess implements UserDAO {
    @Override
    public UserData getUser(String user) throws DataAccessException {
        return null;
    }

    @Override
    public void createUser(RegisterRequest data) throws DataAccessException {

    }

    @Override
    public void deleteUsers() throws DataAccessException {

    }

    @Override
    public HashMap<String, UserData> getUsers() {
        return null;
    }
}
