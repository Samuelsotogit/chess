import chess.*;
import dataaccess.UserMemoryDataAccess;
import server.Server;
import service.UserService;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        Server server = new Server();
        server.run(8080);
        String formattedString = String.format("♕ 240 Chess Server: Server started on port %d", 8080);
        System.out.println(formattedString + ". Open http://localhost:8080 in your browser.");
    }
}