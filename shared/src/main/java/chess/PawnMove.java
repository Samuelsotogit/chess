package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMove implements PieceMoveCalculator {

    /* Maybe use for promotion piece?c*/
    private ChessPiece promotionPiece;

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
            if (board.isInbounds(nextPosition)) {

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

                //Option 1: Forward two only on first move.
                if (pawn.getTeamColor() == ChessGame.TeamColor.WHITE && position.getRow() == 2) {
                    ChessPiece forwardTwoPiece = board.getPiece(forwardTwoPosition);
                    if (nextPiece == null && forwardTwoPiece == null) {
                        moves.add(new ChessMove(position, forwardTwoPosition, null));
                    }
                } else if (pawn.getTeamColor() == ChessGame.TeamColor.BLACK && position.getRow() == 7) {
                    ChessPiece forwardTwoPiece = board.getPiece(forwardTwoPosition);
                    if (nextPiece == null && forwardTwoPiece == null) {
                        moves.add(new ChessMove(position, forwardTwoPosition, null));
                    }
                }
                //Option 2: Forward one.
                if (nextPiece == null) {
                    /*Implement the promotion logic everywhere it needs to go*/
                    if (nextPosition.getRow() == 8 || nextPosition.getRow() == 1) {
                        moves.add(new ChessMove(position, nextPosition, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(position, nextPosition, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, nextPosition, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, nextPosition, ChessPiece.PieceType.KNIGHT));
                    } else {
                        moves.add(new ChessMove(position, nextPosition, null));
                    }
                }
                /* Possible to capture diagonally right */
                if (rightDiagonalPiece == null) {
                    moves.add(new ChessMove(position, diagonallyRight, null));
                    moves.remove(new ChessMove(position, diagonallyRight, null));
                } else if (rightDiagonalPiece.getTeamColor() != pawn.getTeamColor()) {
                    if (diagonallyRight.getRow() == 8 || diagonallyRight.getRow() == 1) {
                        moves.add(new ChessMove(position, diagonallyRight, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(position, diagonallyRight, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, diagonallyRight, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, diagonallyRight, ChessPiece.PieceType.KNIGHT));
                    } else {
                        moves.add(new ChessMove(position, diagonallyRight, null));
                    }
                }
                /* Possible to capture diagonally left */
                if (leftDiagonalPiece == null) {
                    moves.add(new ChessMove(position, diagonallyLeft, null));
                    moves.remove(new ChessMove(position, diagonallyLeft, null));
                } else if (leftDiagonalPiece.getTeamColor() != pawn.getTeamColor()) {
                    if (diagonallyLeft.getRow() == 8 || diagonallyLeft.getRow() == 1) {
                        moves.add(new ChessMove(position, diagonallyLeft, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(position, diagonallyLeft, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, diagonallyLeft, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, diagonallyLeft, ChessPiece.PieceType.KNIGHT));
                    } else {
                        moves.add(new ChessMove(position, diagonallyLeft, null));
                    }
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