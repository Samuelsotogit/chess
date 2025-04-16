package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;

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
        ChessBoard board = getBoard();
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        Collection<ChessMove> possibleMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();
        for (ChessMove move : possibleMoves) {
            ChessPosition endPosition = move.getEndPosition();
            ChessPiece capturedPiece = board.getPiece(endPosition);
            makeMove2(move);
            if (!isInCheck(piece.getTeamColor())) {
                validMoves.add(move);
            }
            undoMove(move, capturedPiece);
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
        ChessBoard board = getBoard();
        ChessPosition fromPosition = new ChessPosition(move.getStartPosition().getRow(), move.getStartPosition().getColumn());
        ChessPiece piece = board.getPiece(fromPosition);
        if (piece == null) {
            throw new InvalidMoveException();
        }
        TeamColor turn = getTeamTurn();
        // Not your turn move
        if (piece.getTeamColor() != turn) {
            throw new InvalidMoveException();
        }
        // Invalid move
        if (!validMoves(fromPosition).contains(move)) {
            throw new InvalidMoveException();
        }
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            if (move.getEndPosition().getRow() == 8 || move.getEndPosition().getRow() == 1) {
                ChessPiece.PieceType promotionPiece = move.getPromotionPiece();
                piece = new ChessPiece(piece.getTeamColor(), promotionPiece);
                board.addPiece(move.getEndPosition(), piece);
                board.removePiece(fromPosition);
                setTeamTurn(turn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
            }
        }
        ChessPosition nextPosition = move.getEndPosition();
        board.addPiece(nextPosition, piece);
        board.removePiece(fromPosition);
        setTeamTurn(turn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
    }

    public void makeMove2(ChessMove move) {
        ChessBoard board = getBoard();
        ChessPosition fromPosition = move.getStartPosition();
        ChessPiece piece = board.getPiece(fromPosition);
        ChessPosition nextPosition = move.getEndPosition();
        board.addPiece(nextPosition, piece);
        board.removePiece(fromPosition);
    }

    public void undoMove(ChessMove move, ChessPiece takenPiece) {
        ChessBoard board = getBoard();
        ChessPosition fromPosition = move.getStartPosition();
        ChessPosition nextPosition = move.getEndPosition();
        ChessPiece piece = board.getPiece(nextPosition);
        board.addPiece(fromPosition, piece);
        board.removePiece(nextPosition);
        if (takenPiece != null) {
            board.addPiece(nextPosition, takenPiece);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessMove> allOpponentMoves = opponentMoves(teamColor);
        ChessPosition kingPosition = findPiecePosition(teamColor, ChessPiece.PieceType.KING);
        for (ChessMove move : allOpponentMoves) {
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
        ChessBoard board = getBoard();
        if (isInCheck(teamColor)) {
            if (canOnlyMoveIntoCheck(teamColor)) {
                if (cannotEliminateCheck(teamColor)) {
                    return cannotBlockCheck(teamColor);
                }
            }
        }
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
        ChessBoard board = getBoard();
        // Grab all the friendly moves
        Collection<ChessMove> friendlyMoves = friendlyMoves(teamColor);
        Iterator<ChessMove> iterator = friendlyMoves.iterator();
        // Remove the king's moves
        while (iterator.hasNext()) {
            ChessMove friendlyMove = iterator.next();
            if (board.getPiece(friendlyMove.getStartPosition()).getPieceType() == ChessPiece.PieceType.KING) {
                iterator.remove();
            }
        }
        // If there are no friendly moves, it means only the king can move
        if (friendlyMoves.isEmpty()) {
            // If the king can only move into check, it is a stalemate
            return canOnlyMoveIntoCheck(teamColor);
        }
        return false;
    }

    private boolean canOnlyMoveIntoCheck(TeamColor teamColor) {
        ChessBoard board = getBoard();
        ChessPiece king = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        ChessPosition kingPosition = findPiecePosition(teamColor, ChessPiece.PieceType.KING);
        Collection<ChessMove> allEnemyMoves = opponentMoves(teamColor);
        Collection<ChessMove> kingMoves = king.pieceMoves(board, kingPosition);
        for (ChessMove kingMove : kingMoves) {
            for (ChessMove enemyMove : allEnemyMoves) {
                if (enemyMove.getEndPosition().equals(kingMove.getEndPosition())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean cannotEliminateCheck(TeamColor teamColor) {
        ChessBoard board = getBoard();
        Collection<ChessPiece> piecesGivingCheck = new ArrayList<>();
        Collection<ChessMove> allOpponentMoves = opponentMoves(teamColor);
        Collection<ChessMove> friendlyMoves = friendlyMoves(teamColor);
        ChessPosition kingPosition = findPiecePosition(teamColor, ChessPiece.PieceType.KING);
        // Find pieces giving check
        for (ChessMove opponentMove : allOpponentMoves) {
            if (opponentMove.getEndPosition().equals(kingPosition)) {
                ChessPiece pieceGivingCheck = board.getPiece(opponentMove.getStartPosition());
                piecesGivingCheck.add(pieceGivingCheck);
            }
        }
        Iterator<ChessPiece> iterator = piecesGivingCheck.iterator();
        // Check if pieces giving check can be captured
        while (iterator.hasNext()) {
            ChessPiece enemyPiece = iterator.next();
            ChessPosition enemyPiecePosition = findPiecePosition(getOppositeColor(), enemyPiece.getPieceType());
            for (ChessMove friendlyMove : friendlyMoves) {
                if (friendlyMove.getEndPosition().equals(enemyPiecePosition)) {
                    makeMove2(friendlyMove);
                    if (!isInCheck(teamColor)) {
                        undoMove(friendlyMove, enemyPiece);
                        iterator.remove();
                        break;
                    }
                    undoMove(friendlyMove, enemyPiece);
                }
            }
        }

        return !piecesGivingCheck.isEmpty();
    }

    private boolean cannotBlockCheck(TeamColor teamColor) {
        ChessBoard board = getBoard();
        Collection<ChessMove> allOpponentMoves = opponentMoves(teamColor);
        Collection<ChessMove> friendlyMoves = friendlyMoves(teamColor);
        ChessPosition kingPosition = findPiecePosition(teamColor, ChessPiece.PieceType.KING);
        Iterator<ChessMove> iterator = friendlyMoves.iterator();
        // Remove the king's moves
        while (iterator.hasNext()) {
            ChessMove friendlyMove = iterator.next();
            if (board.getPiece(friendlyMove.getStartPosition()).getPieceType() == ChessPiece.PieceType.KING) {
                iterator.remove();
            }
        }
        // If my king is the only piece that can move, I can't block the check
        if (friendlyMoves.isEmpty()) {
            return true;
        }
        for (ChessMove opponentMove : allOpponentMoves) {
            if (opponentMove.getEndPosition().equals(kingPosition)) {
                for (ChessMove friendlyMove : friendlyMoves) {
                    makeMove2(friendlyMove);
                    if (!isInCheck(teamColor)) {
                        undoMove(friendlyMove, board.getPiece(friendlyMove.getEndPosition()));
                        return false;
                    }
                    undoMove(friendlyMove, board.getPiece(friendlyMove.getEndPosition()));
                    return true;
                }
            }
        }
        return true;
    }

    private Collection<ChessMove> opponentMoves(TeamColor teamColor) {
        ChessBoard board = getBoard();
        Collection<ChessMove> opposingTeamMoves = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getTeamColor() != teamColor) {
                    opposingTeamMoves.addAll(piece.pieceMoves(board, position));
                }
            }
        }
        return opposingTeamMoves;
    }


    private Collection<ChessMove> friendlyMoves(TeamColor teamColor) {
        ChessBoard board = getBoard();
        // Get the current board
        ChessPosition kingPosition;
        //List of moves of all enemy pieces
        Collection<ChessMove> friendlyMoves = new ArrayList<>();
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
                    if (piece.getTeamColor() == teamColor) {
                        friendlyMoves.addAll(piece.pieceMoves(board, position));
                    }
                }
            }
        }
        return friendlyMoves;
    }


    private ChessPosition findPiecePosition(TeamColor teamColor, ChessPiece.PieceType pieceType) {
        ChessBoard board = getBoard();
        // Get the current board
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition position = new ChessPosition(i, j);
                if (board.getPiece(position) == null) {
                    continue;
                }
                if (board.getPiece(position).getPieceType() != null) {
                    //Examine every piece
                    ChessPiece piece = board.getPiece(position);
                    if (piece.getPieceType() == pieceType && piece.getTeamColor() == teamColor) {
                        return position;
                    }
                }
            }
        }
        return null;
    }

    private TeamColor getOppositeColor() {
        return getTeamTurn() == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        System.out.println("Setting board:\n" + board);
        ChessBoard debugBoard = board;
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
}

