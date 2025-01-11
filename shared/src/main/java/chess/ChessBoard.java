package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    public ChessBoard() {
        ChessPiece.PieceType[][] board;
        board = new ChessPiece.PieceType[8][8];
        for (int i = 0; i < 8; i++) {
            board[1][i] = ChessPiece.PieceType.PAWN;
            board[6][i] = ChessPiece.PieceType.PAWN;
        }

        // Black pieces
        board[0][0] = ChessPiece.PieceType.ROOK;
        board[0][1] = ChessPiece.PieceType.KNIGHT;
        board[0][2] = ChessPiece.PieceType.BISHOP;
        board[0][3] = ChessPiece.PieceType.QUEEN;
        board[0][4] = ChessPiece.PieceType.KNIGHT;
        board[0][5] = ChessPiece.PieceType.BISHOP;
        board[0][6] = ChessPiece.PieceType.KNIGHT;
        board[0][7] = ChessPiece.PieceType.ROOK;

        // White pieces
        board[7][0] = ChessPiece.PieceType.ROOK;
        board[7][1] = ChessPiece.PieceType.KNIGHT;
        board[7][2] = ChessPiece.PieceType.BISHOP;
        board[7][3] = ChessPiece.PieceType.QUEEN;
        board[7][4] = ChessPiece.PieceType.KNIGHT;
        board[7][5] = ChessPiece.PieceType.BISHOP;
        board[7][6] = ChessPiece.PieceType.KNIGHT;
        board[7][7] = ChessPiece.PieceType.ROOK;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Removes a chess piece from the chessboard
     * @param piece the piece to remove
     */
    public void removePiece(ChessPiece piece) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        throw new RuntimeException("Not implemented");
    }
}
