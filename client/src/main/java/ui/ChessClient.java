package ui;
import model.AuthData;
import model.GameID;
import server.ServerFacade;
import shared.ResponseException;
import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {
    private final String serverUrl;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private String username;
    private String authToken;

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
                    case "list" -> listGames();
                    case "join" -> joinGame(params);
                    case "observe" -> observeGame();
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
        AuthData res = server.login(params[0], params[1]);
        return authenticate(res);
    }

    public Integer createGame(String... params) throws ResponseException {
        String gameName = params[0];
        return server.createGame(authToken, gameName);
    }

    public String listGames(String... params) throws ResponseException {
        return server.listGames(params[0], params[1]).toString();
    }

    public String joinGame(String... params) throws ResponseException {
        server.joinGame(params[0], params[1], params[2]);
        return "Join game";
    }

    public String observeGame() {
        return "Observe game";
    }

    public String logout() throws ResponseException {
        server.logout(username, authToken);
        username = null;
        authToken = null;
        state = State.SIGNEDOUT;
        return "Logged out";
    }

    public String exitProgram() {
        return "Goodbye!";
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
                help - with commands
                """;
        } else if (state == State.SIGNEDIN) {
            return """
                Commands:
                create <NAME> - a game
                list - games
                join <gameID> [WHITE|BLACK] - a game
                observe <gameID> - a game
                logout - when you are done
                """;
        }
        return """
                Commands:
                help - with commands""";
    }
}
