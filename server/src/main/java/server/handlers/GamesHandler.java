package server.handlers;

import chess.ChessGame;
import data.transfer.objects.GameRequest;
import data.transfer.objects.JoinGameRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import server.ResponseException;
import model.ErrorData;
import model.GameData;
import model.GameID;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class GamesHandler {

    GameService service;

    public GamesHandler(GameService service) {
        this.service = service;
    }

    public Object listGames(Request request, Response response) throws ResponseException {
        String authToken = request.headers("authorization");
        Collection<GameData> games;
        JsonObject jsonObject = new JsonObject();
        try {
            games = service.listGames(authToken);
        } catch (ResponseException e) {
            response.status(401);
            return new Gson().toJson(new ErrorData("Error: unauthorized"));
        }
        response.status(200);
        jsonObject.add("games", new Gson().toJsonTree(games));
        return new Gson().toJson(jsonObject);
    }

    public Object createGame(Request request, Response response) throws ResponseException {
        String authToken = request.headers("authorization");
        String gameName = new Gson().fromJson(request.body(), GameData.class).gameName();
        GameRequest gameRequest = new GameRequest(gameName, authToken);
        GameID gameId = service.createGame(gameRequest);
        response.status(200);
        return new Gson().toJson(gameId);
    }

    public Object joinGame(Request request, Response response) throws ResponseException {
        String authToken = request.headers("authorization");
        JoinGameRequest gameRequest = new Gson().fromJson(request.body(), JoinGameRequest.class);
        service.joinGame(authToken, gameRequest.playerColor(), gameRequest.gameID());
        response.status(200);
        return "";
    }
}
