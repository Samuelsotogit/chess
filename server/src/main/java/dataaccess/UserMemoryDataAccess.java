package dataaccess;

public class UserMemoryDataAccess implements UserDAO {
    @Override
    public Object getUser(Object user) throws DataAccessException {
        return user;
    }
}
