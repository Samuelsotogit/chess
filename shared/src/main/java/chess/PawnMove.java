package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMove implements PieceMoveCalculator {

    @Override
    public Collection<ChessMove> possibleMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(position);
        if (piece == null) {
            return moves;
        }

        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            ChessPiece pawn = piece;

            int[][] Directions = {
                    {1, 0},
            };

            int rowIncrement = Directions[0][0];
            int columnIncrement = Directions [0][1];
            int i = 1;

            //Initialize diagonal positions to check if you can capture.
            ChessPosition diagonallyRight = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
            ChessPosition diagonallyLeft = new ChessPosition(position.getRow() + 1, position.getColumn() - 1);

            //Update direction if pawn is black
            if (pawn.getTeamColor() == ChessGame.TeamColor.BLACK) {
                rowIncrement = Directions[0][0] * -1;
                diagonallyRight = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
                diagonallyLeft = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
            }

            ChessPosition nextPosition = new ChessPosition(position.getRow() + i * rowIncrement, position.getColumn() + i * columnIncrement);
            ChessPosition forwardTwoPosition = new ChessPosition(nextPosition.getRow() + rowIncrement, nextPosition.getColumn());

            //Only add moves while you are in bounds
            while (board.isInbounds(nextPosition) && board.isInbounds(forwardTwoPosition)) {

                //Check if you can assign the diagonal pieces
                ChessPiece rightDiagonalPiece = null;
                ChessPiece leftDiagonalPiece = null;
                if (board.isInbounds(diagonallyRight)) {
                    rightDiagonalPiece = board.getPiece(diagonallyRight);
                }
                if (board.isInbounds(diagonallyLeft)) {
                    leftDiagonalPiece = board.getPiece(diagonallyLeft);
                }

                ChessPiece nextPiece = board.getPiece(nextPosition);
                ChessPiece forwardTwoPiece = board.getPiece(forwardTwoPosition);

                //Option 1: Forward two only on first move.
                if (pawn.getTeamColor() == ChessGame.TeamColor.WHITE && position.getRow() == 2) {
                    if (nextPiece == null && forwardTwoPiece == null) {
                        moves.add(new ChessMove(position, forwardTwoPosition, null));
                    }
                }
                else if (pawn.getTeamColor() == ChessGame.TeamColor.BLACK && position.getRow() == 7) {
                    if (nextPiece == null && forwardTwoPiece == null) {
                        moves.add(new ChessMove(position, forwardTwoPosition, null));
                    }
                }
                //Option 2: Forward one.
                if (nextPiece == null) {
                    moves.add(new ChessMove(position, nextPosition, null));
                }
                /* Possible to capture diagonally right */
                if (rightDiagonalPiece == null) {
                    break;
                } else if (rightDiagonalPiece.getTeamColor() != pawn.getTeamColor()) {
                    moves.add(new ChessMove(position, diagonallyRight, null));
                    break;
                }
                /* Possible to capture diagonally left */
                if (leftDiagonalPiece == null) {
                    break;
                } else if (leftDiagonalPiece.getTeamColor() != pawn.getTeamColor()) {
                    moves.add(new ChessMove(position, diagonallyLeft, null));
                    break;
                }
                /* do nothing if next square forward has friendly piece */
            }
            //Option 1: Forward two only on first move.
            //Option 2: Forward one.

            //Option 3: Diagonally one if enemy piece is diagonally place 1 square away.

            //Extra credit 'EN PASSANT': Diagonally one IF a pawn type enemy piece does OPTION 1 and IF that same enemy piece is adjacent
            // and IF we have not made any previous moves.
        }
        return moves;
    }
}