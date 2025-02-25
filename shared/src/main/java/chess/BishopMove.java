package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMove implements PieceMoveCalculator {

    @Override
    public Collection<ChessMove> possibleMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(position);
        if (piece == null) {
            return moves;
        }
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {

            // Use direction vectors to iterate through all possible moves.
            int[][] directions = {
                    {1, 1},
                    {-1, 1},
                    {-1, -1},
                    {1, -1}
            };
            board.getMoves(directions, moves, piece, position);
        }
        return moves;

    }
}
