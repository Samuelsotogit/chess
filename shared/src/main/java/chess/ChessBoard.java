package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] board;

    public ChessBoard() {
        board = new ChessPiece[8][8];
    }

        public void setUpBoard() {

            //Initialize white Pieces.
            ChessPiece whitePawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            ChessPiece whiteRook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
            ChessPiece whiteKnight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
            ChessPiece whiteBishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
            ChessPiece whiteQueen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
            ChessPiece whiteKing = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);

            //Initialize black pieces
            ChessPiece blackPawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            ChessPiece blackRook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
            ChessPiece blackKnight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
            ChessPiece blackBishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
            ChessPiece blackQueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
            ChessPiece blackKing = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);

            // Initialize the starting board
            for (int i = 0; i < 8; i++) {
                board[1][i] = whitePawn;
                board[6][i] = blackPawn;
            }

            // White pieces
            board[0][0] = whiteRook;
            board[0][1] = whiteKnight;
            board[0][2] = whiteBishop;
            board[0][3] = whiteQueen;
            board[0][4] = whiteKing;
            board[0][5] = whiteBishop;
            board[0][6] = whiteKnight;
            board[0][7] = whiteRook;

            // Black pieces
            board[7][0] = blackRook;
            board[7][1] = blackKnight;
            board[7][2] = blackBishop;
            board[7][3] = blackQueen;
            board[7][4] = blackKing;
            board[7][5] = blackBishop;
            board[7][6] = blackKnight;
            board[7][7] = blackRook;

            // Set empty squares
            for (int i = 0; i < 8; i++) {
                board[2][i] = null;
                board[3][i] = null;
                board[4][i] = null;
                board[5][i] = null;
            }
        }
    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        this.board[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Removes a chess piece from the chessboard
     * @param position the position to remove the piece from
     */
    public void removePiece(ChessPosition position) {
        this.board[position.getRow()-1][position.getColumn()-1] = null;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return this.board[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        setUpBoard();
    }

    /**
     * @param myPosition the position my piece is in.
     * @return true if my position is within bounds, else false.
     */
    public boolean isInBounds(ChessPosition myPosition) {
        return myPosition.getRow() <= 8 && myPosition.getColumn() <= 8 && myPosition.getColumn() >= 1 && myPosition.getRow() >= 1;
    }

    /**
     * @param directions the direction vectors to iterate through all possible moves.
     */
    public void getMoves(int[][] directions, Collection<ChessMove> moves, ChessPiece piece, ChessPosition position) {
        for (int[] direction : directions) {
            int rowIncrement = direction[0];
            int columnIncrement = direction[1];
            int i = 1;

            ChessPosition nextPosition = new ChessPosition(position.getRow() + i * rowIncrement, position.getColumn() + i * columnIncrement);

            while (isInBounds(nextPosition)) {
                ChessPiece nextPiece = getPiece(nextPosition);
                // If there are no pieces ahead, those are valid moves.
                if (nextPiece == null) {
                    moves.add(new ChessMove(position, nextPosition, null));
                    // If the piece is a knight or king, it can only move one square.
                    if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT || piece.getPieceType() == ChessPiece.PieceType.KING) {
                        break;
                    }
                }
                //As soon as there is an enemy piece in the next column, that rook can be moved to that position as a valid move (taking).
                else if (nextPiece.getTeamColor() != piece.getTeamColor()) {
                    moves.add(new ChessMove(position, nextPosition, null));
                    //Stop adding moves if capturing a piece.
                    break;
                }
                else if (nextPiece.getTeamColor() == piece.getTeamColor()) {
                    //Stop adding moves if blocked by a piece of the same color.
                    break;
                }
                //Update square being checked as possible move.
                i++;
                nextPosition = new ChessPosition(position.getRow() + i * rowIncrement, position.getColumn() + i * columnIncrement);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 8; i >= 1; i--) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = getPiece(position);
                if (piece != null) {
                    char pieceChar = piece.getPieceType() == ChessPiece.PieceType.KNIGHT ? 'n' : piece.getPieceType().toString().charAt(0);
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        pieceChar = Character.toUpperCase(pieceChar);
                    } else {
                        pieceChar = Character.toLowerCase(pieceChar);
                    }
                    sb.append(pieceChar);
                } else {
                    sb.append("|");
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
