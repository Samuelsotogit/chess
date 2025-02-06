package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;
    private ChessPosition position;
    private ChessPiece piece;

    public ChessGame() {
        this.board = new ChessBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = new ChessPiece(board.getPiece(startPosition).getTeamColor(), board.getPiece(startPosition).getPieceType());
        return piece.pieceMoves(board, startPosition);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */

    /* When is it invalid to make a move?
    * -After the game is over: There is a checkmate one either team, there is a stalemate on either team.
    * -When the king's next move is also a possible move of an attacking piece (Check).
    * -When it is not your turn.
    * */

    public void makeMove(ChessMove move)
            throws InvalidMoveException {
        ChessPosition fromPosition = new ChessPosition(move.getStartPosition().getRow(), move.getStartPosition().getColumn());
        ChessPiece piece = new ChessPiece(board.getPiece(fromPosition).getTeamColor(), board.getPiece(fromPosition).getPieceType());
        TeamColor turn = getTeamTurn();
        // Not your turn move
        if (piece.getTeamColor() != turn) {
            throw new InvalidMoveException();
        } else if (isInCheckmate(piece.getTeamColor())) {
            throw new InvalidMoveException();
        } else if (isInStalemate(piece.getTeamColor())) {
            throw new InvalidMoveException();
        } else if (isInCheck(piece.getTeamColor())) {
            throw new InvalidMoveException();
        } else {
            ChessPosition nextPosition = new ChessPosition(move.getEndPosition().getRow(), move.getEndPosition().getColumn());
            board.addPiece(nextPosition, piece);
            board.removePiece(fromPosition);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        board.setUpBoard();
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
