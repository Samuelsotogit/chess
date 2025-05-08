package websocket.commands;
import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private final ChessMove move;

    public MakeMoveCommand(CommandType commandType, String authToken, Integer gameId, ChessMove move) {
        super(commandType, authToken, gameId);
        this.move = move;
    }

    public ChessMove getMove() {
        return move;
    }
}
