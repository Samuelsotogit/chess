package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMove implements PieceMoveCalculator{

    @Override
    public Collection<ChessMove> possibleMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(position);
        if (piece == null) {
            return moves;
        }
        if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            ChessPiece rook = piece;
            // If there are no pieces ahead, those are valid moves.
            int i = 1;
            ChessPosition tempPosition = new ChessPosition(position.getRow()+i, position.getColumn());
            while (isInBounds(tempPosition)) {
                ChessPiece nextPiece = board.getPiece(tempPosition);
                if (nextPiece == null) {
                    moves.add(new ChessMove(position, tempPosition, null));
                    i++;
                }
                //As soon as there is a piece in the next column, that rook can be moved to that position as a valid move (taking).
                else if (nextPiece.getTeamColor() != rook.getTeamColor()) {
                    moves.add(new ChessMove(position, tempPosition, null));
                    //Stop adding moves if capturing a piece.
                    return moves;
                }
                //Update square being checked as possible move.
                tempPosition = new ChessPosition(position.getRow() + i, position.getColumn());

            }
        }
        return moves;
    }
    private boolean isInBounds(ChessPosition myPosition) {
        return myPosition.getRow() <= 8 && myPosition.getColumn() <= 8 && myPosition.getColumn() >= 1 && myPosition.getRow() >= 1;
    }
}
