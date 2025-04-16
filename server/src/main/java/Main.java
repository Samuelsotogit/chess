import chess.*;
import dataaccess.DataAccessException;
import shared.ResponseException;
import server.Server;

public class Main {
    public static void main(String[] args) throws ResponseException, DataAccessException {
        Server server = new Server();
        server.run(8080);
        String formattedString = String.format("♕ 240 Chess Server: Server started on port %d", 8080);
        System.out.println(formattedString + ". Open http://localhost:8080 in your browser.");
    }
}