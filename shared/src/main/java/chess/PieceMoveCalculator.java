package chess;

import java.util.Collection;

public interface PieceMoveCalculator {
    Collection<ChessMove>possibleMoves(ChessBoard board, ChessPosition position);
}

