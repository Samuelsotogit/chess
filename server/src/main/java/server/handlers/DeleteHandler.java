package server.handlers;
import server.ResponseException;
import service.UserService;
import spark.Request;
import spark.Response;

public class DeleteHandler {

    UserService service;

    public DeleteHandler(UserService service) {
        this.service = service;
    }

    public Object clearDatabase(Request request, Response response) throws ResponseException {
        service.clearDatabase();
        response.status(200);
        return "";
    }
}
