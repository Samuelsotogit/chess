package server.handlers;

import data.transfer.objects.LoginRequest;
import data.transfer.objects.RegisterOrLoginResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import shared.ResponseException;
import service.UserService;
import spark.Request;
import spark.Response;

public class LoginHandler {

    UserService service;

    public LoginHandler(UserService service) {
        this.service = service;
    }

    public Object loginUser(Request request, Response response) throws ResponseException {
        LoginRequest loginRequest = new Gson().fromJson(request.body(), LoginRequest.class);
        RegisterOrLoginResponse result;
        try {
            validateUser(loginRequest);
            result = service.login(loginRequest);
        } catch (JsonSyntaxException e) {
            throw new ResponseException(400, "Error: unauthorized");
        }
        response.status(200);
        return new Gson().toJson(result);
    }

    public Object logout(Request request, Response response) throws ResponseException {
        String authToken = request.headers("authorization");
        service.logout(authToken);
        response.status(200);
        return "";
    }

    public void validateUser(LoginRequest request) throws JsonSyntaxException {
        if (request.username() == null || request.password() == null) {
            throw new JsonSyntaxException("Error: Bad Request");
        }
    }
}
