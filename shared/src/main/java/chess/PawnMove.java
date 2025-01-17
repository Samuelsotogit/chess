package chess;

import java.util.Collection;

public class PawnMove implements PieceMoveCalculator {
    @Override
    public Collection<ChessMove> possibleMoves(ChessBoard board, ChessPosition position) {
        //Option 1: Forward two only on first move.

        //Option 2: Forward one.

        //Option 3: Diagonally one if enemy piece is diagonally place 1 square away.

        //Option 4: Diagonally one IF a pawn type enemy piece does OPTION 1 and IF that same enemy piece is adjacent
        // and IF we have not made any previous moves.

        throw new RuntimeException("Not implemented");
    }
}
