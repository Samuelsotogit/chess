package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor color;
    private ChessPiece.PieceType typeOfPiece;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.typeOfPiece = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.typeOfPiece;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch (typeOfPiece) {
            case PieceType.PAWN -> {
                return new PawnMove().possibleMoves(board, myPosition);
            }
            case PieceType.ROOK -> {
                return new RookMove().possibleMoves(board, myPosition);
            }
            case PieceType.KNIGHT -> {
                return new KnightMove().possibleMoves(board, myPosition);
            }
            case PieceType.BISHOP -> {
                return new BishopMove().possibleMoves(board, myPosition);
            }
            case PieceType.QUEEN -> {
                return new QueenMove().possibleMoves(board, myPosition);
            }
            case PieceType.KING -> {
                return new KingMove().possibleMoves(board, myPosition);
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return color == that.color && typeOfPiece == that.typeOfPiece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, typeOfPiece);
    }

    @Override
    public String toString() {
        return "ChessPiece = " + color + " " + typeOfPiece;
    }
}
