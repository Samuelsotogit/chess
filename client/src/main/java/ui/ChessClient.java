package ui;
import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.AuthData;
import model.GameData;
import model.GameID;
import server.ServerFacade;
import shared.ResponseException;
import ui.PrintableBoard.*;

import java.util.*;

public class ChessClient {
    private final String serverUrl;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private String username;
    private String authToken;
    private final PrintableBoard board = new PrintableBoard(new ChessGame());

    public ChessClient(String serverUrl) {
        this.serverUrl = serverUrl;
        server = new ServerFacade(serverUrl);
    }

    public Object eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (state == State.SIGNEDOUT) {
                return switch (cmd) {
                    case "register" -> register(params);
                    case "login" -> login(params);
                    case "quit" -> exitProgram();
                    default -> help();
                };
            } else if (state == State.SIGNEDIN) {
                return switch (cmd) {
                    case "create" -> createGame(params);
                    case "list" -> listGames(params);
                    case "join" -> joinGame(params);
                    case "observe" -> observeGame(params);
                    case "logout" -> logout();
                    case "quit" -> exitProgram();
                    default -> help();
                };
            } return "";
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public AuthData register(String... params) throws Exception {
        AuthData res = server.register(params[0], params[1], params[2]);
        return authenticate(res);
    }

    public AuthData login(String... params) throws ResponseException {
        if (params.length != 2) {
            return null;
        }
        AuthData res = server.login(params[0], params[1]);
        return authenticate(res);
    }

    public String createGame(String... params) throws ResponseException {
        String gameName = params[0];
        GameID gameID = server.createGame(gameName, authToken);
        if (gameID.gameID() == null) {
            return "Something went wrong";
        }
        return String.format("You created a new game: %s", gameName);
    }

    public String listGames(String... params) throws ResponseException {
        Collection<GameData> games = server.listGames(username, authToken);
        int index = 1;
        StringBuilder result = new StringBuilder();
        for (GameData game : games) {
            if (index == games.size()) {
                result.append(String.format("%d-->Game: %s, White player: %s, Black player: %s",
                        index,
                        game.gameName(),
                        game.whiteUsername() == null ? "AVAILABLE" : game.whiteUsername(),
                        game.blackUsername() == null ? "AVAILABLE" : game.blackUsername()));
                index++;
            } else {
            result.append(String.format("%d-->Game: %s, White player: %s, Black player: %s\n",
                    index,
                    game.gameName(),
                    game.whiteUsername() == null ? "AVAILABLE" : game.whiteUsername(),
                    game.blackUsername() == null ? "AVAILABLE" : game.blackUsername()));
            index++;
            }
        }
        return result.toString();
    }

    public String joinGame(String... params) throws ResponseException {
        if (params.length != 2) {
            return "Error: Incorrect arguments";
        }
        server.joinGame(authToken, params[1], params[0]);
        Integer id = Integer.parseInt(params[0]);
        ChessGame.TeamColor color;
        if (Objects.equals(params[1], "white")) {
            color = ChessGame.TeamColor.WHITE;
        }
        else {
            color = ChessGame.TeamColor.BLACK;
        }
        return board.stringify(color);
    }

    public String observeGame(String... params) throws ResponseException {
        int id = Integer.parseInt(params[0]);
        Collection<GameData> games = server.listGames(username, authToken);
        if (id > games.size() || id < games.size()) {
            return "Game not found";
        }
        //Figure out how to retrieve the right board.
        return board.stringify(ChessGame.TeamColor.WHITE);
    }

    public String logout() throws ResponseException {
        server.logout(username, authToken);
        username = null;
        authToken = null;
        state = State.SIGNEDOUT;
        return "Logged out";
    }

    public String exitProgram() {
        return "quit";
    }

    private AuthData authenticate(AuthData res) {
        username = res.username();
        authToken = res.authToken();
        state = State.SIGNEDIN;
        return res;
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                Commands:
                register <username> <password> <email>
                login <username> <password>
                quit - playing chess
                help - with commands""";
        } else if (state == State.SIGNEDIN) {
            return """
                Commands:
                create <NAME> - a game
                list - games
                join <gameID> [WHITE|BLACK] - a game
                observe <gameID> - a game
                logout - when you are done""";
        }
        return """
                Commands:
                help - with commands""";
    }
}
