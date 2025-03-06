package server.handlers;
import DataTransferObjects.RegisterOrLoginResponse;
import DataTransferObjects.RegisterRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import server.ResponseException;
import service.UserService;
import spark.Request;
import spark.Response;

public class RegistrationHandler {

    UserService service;

    public RegistrationHandler(UserService service) {
        this.service = service;
    }

    public Object registerUser(Request request, Response response) throws ResponseException {
        try {
            // Serialize and Deserialize here.
            RegisterRequest registerRequest = new Gson().fromJson(request.body(), RegisterRequest.class);
            validateUser(registerRequest);
            RegisterOrLoginResponse authData= service.register(registerRequest);
            response.status(200);
            return new Gson().toJson(authData);
        }
        catch (JsonSyntaxException e) {
            throw new ResponseException(400, "Error: Bad Request");
        }
    }

    public void validateUser(RegisterRequest request) throws JsonSyntaxException {
        if (request.username() == null || request.password() == null || request.email() == null) {
            throw new JsonSyntaxException("Error: Bad Request");
        }
    }
}
