package server;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.Request;
import model.AuthData;
import data.transfer.objects.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collection;

import model.GameData;
import model.GameID;
import org.junit.jupiter.api.Assertions;
import shared.ResponseException;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData register(String username, String password, String email) throws Exception {
        var path = "/user";
        var request = new RegisterRequest(username, password, email);
        return this.makeRequest("POST", path, request, null, AuthData.class);
    }

    public AuthData login(String username, String password) throws ResponseException {
        var path = "/session";
        var request = new LoginRequest(username, password);
        return this.makeRequest("POST", path, request, null, AuthData.class);
    }

    public void logout(String username, String authToken) throws ResponseException {
        var path = "/session";
        var request = new AuthData(username, authToken);
        this.makeRequest("DELETE", path, request.authToken(), authToken, null);
    }

    public Collection<GameData> listGames(String username, String authToken) throws ResponseException {
        var path = "/game";
        JsonObject jsonObject = new Gson().toJsonTree(this.makeRequest("GET", path, null, authToken, Object.class)).getAsJsonObject();
        JsonArray gamesArray = jsonObject.getAsJsonArray("games");
        return new Gson().fromJson(gamesArray, new TypeToken<Collection<GameData>>() {}.getType());
    }

    public GameID createGame(String gameName, String authToken) throws ResponseException {
        var path = "/game";
        GameRequest request = new GameRequest(gameName, authToken);
        return this.makeRequest("POST", path, request, authToken, GameID.class);
    }

    public void joinGame(String authToken, String playerColor, String gameID) throws ResponseException {
        var path = "/game";
        ChessGame.TeamColor color = playerColor.equals("white") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
        int id;
        try {
            id = Integer.parseInt(gameID);
        } catch (NumberFormatException e) {
            throw new ResponseException(400, "Invalid game ID format: " + gameID);
        }
        var request = new JoinGameRequest(authToken, color, id);
        this.makeRequest("PUT", path, request, authToken, null);
    }

    private <T> T makeRequest(String method, String path, Object request, String authToken, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null) {
                http.setRequestProperty("Authorization", authToken);
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    public void clear() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }
}
