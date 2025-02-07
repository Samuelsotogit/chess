package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.IllegalFormatCodePointException;
import java.util.Objects;

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

    public ChessGame() {
        this.board = new ChessBoard();
        board.setUpBoard();
        this.teamTurn = TeamColor.WHITE;
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
        if (piece.getPieceType() == null) {
            return null;
        }
        Collection<ChessMove> possibleMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();
        for (ChessMove move : possibleMoves) {
            //How can the king be placed in danger?
            // 1. move the king into the possible moves of an enemy piece.
            // 2. moving a friendly piece will leave the king in the possible moves of an enemy piece.
            if (!isInCheck(teamTurn)) {
                validMoves.add(move);
            } else if (!isInCheckmate(teamTurn)) {
                // Check that the next move does not put the king in check.
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
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
        if (board.getPiece(fromPosition) == null) {
            throw new InvalidMoveException();
        }
        ChessPiece piece = new ChessPiece(board.getPiece(fromPosition).getTeamColor(), board.getPiece(fromPosition).getPieceType());
        TeamColor turn = getTeamTurn();
        // Not your turn move
        if (piece.getTeamColor() != turn) {
            throw new InvalidMoveException();
        }
        // Invalid move
        else if (validMoves(fromPosition).contains(move)) {
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {

                if (move.getEndPosition().getRow() == 8 || move.getEndPosition().getRow() == 1) {
                    ChessPiece.PieceType promotionPiece = move.getPromotionPiece();
                    piece = new ChessPiece(piece.getTeamColor(), promotionPiece);
                    board.addPiece(move.getEndPosition(), piece);
                    board.removePiece(fromPosition);
                    setTeamTurn(turn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
                }
            }
            ChessPosition nextPosition = new ChessPosition(move.getEndPosition().getRow(), move.getEndPosition().getColumn());
            board.addPiece(nextPosition, piece);
            board.removePiece(fromPosition);
            setTeamTurn(turn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
        } else {
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessMove> enemyPiecesPotentialMoves = opponentMoves(teamColor);
        ChessPosition kingPosition = findKingPosition(teamColor);
        for (ChessMove move : enemyPiecesPotentialMoves) {
            if (move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean checkmate = false;
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    private Collection<ChessMove> opponentMoves(TeamColor teamColor) {
        // Get the current board
        ChessBoard board = getBoard();
        ChessPosition kingPosition;
        //List of moves of all enemy pieces
        Collection<ChessMove> opposingTeamMoves = new ArrayList<>();
        // Iterate over entire board
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition position = new ChessPosition(i, j);
                if (board.getPiece(position) == null) {
                    continue;
                }
                if (board.getPiece(position).getPieceType() != null) {
                    //Examine every piece
                    ChessPiece piece = board.getPiece(position);
                    // If the piece is an enemy piece, add its possible moves to the list.
                    if (piece.getTeamColor() != teamColor) {
                        opposingTeamMoves.addAll(piece.pieceMoves(board, position));
                    }
                }
            }
        }
        return opposingTeamMoves;
    }

    private ChessPosition findKingPosition(TeamColor teamColor) {
        // Get the current board
        ChessBoard board = getBoard();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition position = new ChessPosition(i, j);
                if (board.getPiece(position) == null) {
                    continue;
                }
                if (board.getPiece(position).getPieceType() != null) {
                    //Examine every piece
                    ChessPiece piece = board.getPiece(position);
                    if (piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                        return position;
                    }
                }
            }
        }
        return null;
    }
}

