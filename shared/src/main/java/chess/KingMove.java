package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMove implements PieceMoveCalculator {
    @Override
    public Collection<ChessMove> possibleMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(position);
        if (piece == null) {
            return moves;
        }
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            ChessPiece king = piece;

            // Use direction vectors to iterate through all possible moves.
            int[][] directions = {
                    {1, 1},
                    {-1, 1},
                    {-1, -1},
                    {1, -1},
                    {1, 0},
                    {-1, 0},
                    {0, 1},
                    {0, -1}
            };

            // Iterate through all possible directions.
            for (int[] direction : directions) {
                int rowIncrement = direction[0];
                int columnIncrement = direction[1];
                int i = 1;

                ChessPosition nextPosition = new ChessPosition(position.getRow() + i * rowIncrement, position.getColumn() + i * columnIncrement);

                while (isInBounds(nextPosition)) {
                    ChessPiece nextPiece = board.getPiece(nextPosition);
                    // If there are no pieces ahead, those are valid moves.
                    if (nextPiece == null) {
                        moves.add(new ChessMove(position, nextPosition, null));
                        break;
                    }
                    //If there is a piece in the next position, that king can be moved to that position as a valid move (taking).
                    else if (nextPiece.getTeamColor() != king.getTeamColor()) {
                        moves.add(new ChessMove(position, nextPosition, null));
                        //Stop adding moves if capturing a piece.
                        break;
                    }
                    else if (nextPiece.getTeamColor() == king.getTeamColor()) {
                        //Stop adding moves if blocked by a piece of the same color.
                        break;
                    }
                    //Update square being checked as possible move.
                    i++;
                    nextPosition = new ChessPosition(position.getRow() + i * rowIncrement, position.getColumn() + i * columnIncrement);
                }
            }
        }
        return moves;

    }
    private boolean isInBounds(ChessPosition myPosition) {
        return myPosition.getRow() <= 8 && myPosition.getColumn() <= 8 && myPosition.getColumn() >= 1 && myPosition.getRow() >= 1;
    }
}
