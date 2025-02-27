package dataaccess;
import model.UserData.*;

public interface UserDAO {
    Object getUser(Object user) throws DataAccessException;
}
