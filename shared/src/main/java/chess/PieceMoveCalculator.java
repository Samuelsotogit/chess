package chess;

import java.util.Collection;

public interface PieceMoveCalculator {
    Collection<ChessMove>move(ChessBoard board, ChessPosition position);
}

