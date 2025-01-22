package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private ChessPosition fromPosition;
    private ChessPosition toPosition;
    private ChessPiece.PieceType PieceToPromoteTo;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.fromPosition = startPosition;
        this.toPosition = endPosition;
        this.PieceToPromoteTo = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return this.fromPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return this.toPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        if (this.PieceToPromoteTo == null) { //How would you get the promotion piece?
            return null;
        }
        return this.PieceToPromoteTo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(fromPosition, chessMove.fromPosition) && Objects.equals(toPosition, chessMove.toPosition) && PieceToPromoteTo == chessMove.PieceToPromoteTo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromPosition, toPosition, PieceToPromoteTo);
    }

    @Override
    public String toString() {
        return
                fromPosition + " -> " + toPosition +
                " [" + PieceToPromoteTo + "]";
    }
}
