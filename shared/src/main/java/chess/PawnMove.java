package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMove implements PieceMoveCalculator {
    @Override
    public Collection<ChessMove> possibleMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();

        ChessPiece piece = new ChessPiece(board.getPiece(position).getTeamColor(), board.getPiece(position).getPieceType());

        if (piece.getPieceType() != ChessPiece.PieceType.PAWN) {
            return moves;
        }

            int[][] directions = {
                    {1, 0}
            };

            int rowIncrement = directions[0][0];
            int colIncrement = directions[0][1];
            int i = 1;

            ChessPosition diagonallyRight = new ChessPosition(position.getRow()+1, position.getColumn()+1);
            ChessPosition diagonallyLeft = new ChessPosition(position.getRow()+1, position.getColumn()-1);

            if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                rowIncrement = directions[0][0] * -1;
                diagonallyRight = new ChessPosition(position.getRow()-1, position.getColumn()-1);
                diagonallyLeft = new ChessPosition(position.getRow()-1, position.getColumn()+1);
            }

            ChessPosition nextPosition = new ChessPosition(position.getRow() + i * rowIncrement, position.getColumn() + i * colIncrement);
            ChessPosition forwardTwo = new ChessPosition(nextPosition.getRow() + i * rowIncrement, nextPosition.getColumn());

            if (board.isInBounds(nextPosition)) {
                ChessPiece nextPiece = board.getPiece(nextPosition);
                //Forward two:
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE && position.getRow() == 2) {
                    if (nextPiece == null && board.getPiece(forwardTwo) == null) {
                        moves.add(new ChessMove(position, forwardTwo, null));
                    }
                } else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK && position.getRow() == 7) {
                    if (nextPiece == null && board.getPiece(forwardTwo) == null) {
                        moves.add(new ChessMove(position, forwardTwo, null));
                    }
                }
                // Forward one:
                if (nextPiece == null) {
                    if (nextPosition.getRow() == 8 || nextPosition.getRow() == 1) {
                        moves.add(new ChessMove(position, nextPosition, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, nextPosition, ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(position, nextPosition, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, nextPosition, ChessPiece.PieceType.QUEEN));
                    } else {
                        moves.add(new ChessMove(position, nextPosition, null));
                    }
                }
                //Capture right
                if (board.isInBounds(diagonallyRight)) {
                    ChessPiece rightDiagonalPiece = board.getPiece(diagonallyRight);
                    if (rightDiagonalPiece != null && rightDiagonalPiece.getTeamColor() != piece.getTeamColor()) {
                        if (diagonallyRight.getRow() == 8 || diagonallyRight.getRow() == 1) {
                            moves.add(new ChessMove(position, diagonallyRight, ChessPiece.PieceType.ROOK));
                            moves.add(new ChessMove(position, diagonallyRight, ChessPiece.PieceType.KNIGHT));
                            moves.add(new ChessMove(position, diagonallyRight, ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(position, diagonallyRight, ChessPiece.PieceType.QUEEN));
                        } else {
                            moves.add(new ChessMove(position, diagonallyRight, null));
                        }
                    }
                }
                //Capture Left
                if (board.isInBounds(diagonallyLeft)) {
                    ChessPiece leftDiagonalPiece = board.getPiece(diagonallyLeft);
                    if (leftDiagonalPiece != null && leftDiagonalPiece.getTeamColor() != piece.getTeamColor()) {
                        if (diagonallyLeft.getRow() == 8 || diagonallyLeft.getRow() == 1) {
                            moves.add(new ChessMove(position, diagonallyLeft, ChessPiece.PieceType.ROOK));
                            moves.add(new ChessMove(position, diagonallyLeft, ChessPiece.PieceType.KNIGHT));
                            moves.add(new ChessMove(position, diagonallyLeft, ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(position, diagonallyLeft, ChessPiece.PieceType.QUEEN));
                        } else {
                            moves.add(new ChessMove(position, diagonallyLeft, null));
                        }
                    }
                }
            }
            return moves;
    }
}