package chess;

import java.util.Collection;

/**
 * Calculates all the positions a chess piece can move to
 * Does not take into account moves that are illegal due to leaving the king in
 * danger
 */
public class PieceMoveCalculator {

    /**
     * @return Collection of valid King moves
     */
    public Collection<ChessMove> KingMoveCalculator(ChessBoard board, ChessPosition MyPosition) {
        throw new RuntimeException("not implemented");
    }

    /**
     * @return Collection of valid Queen moves
     */
    public Collection<ChessMove> QueenMoveCalculator(ChessBoard board, ChessPosition MyPosition) {
        throw new RuntimeException("not implemented");
    }

    /**
     * @return Collection of valid Rook moves
     */
    public Collection<ChessMove> RookMoveCalculator(ChessBoard board, ChessPosition MyPosition) {
        throw new RuntimeException("not implemented");
    }

    /**
     * @return Collection of valid Bishop moves
     */
    public Collection<ChessMove> BishopMoveCalculator(ChessBoard board, ChessPosition MyPosition) {
        throw new RuntimeException("not implemented");
    }

    /**
     * @return Collection of valid Knight moves
     */
    public Collection<ChessMove> KnightMoveCalculator(ChessBoard board, ChessPosition MyPosition) {
        throw new RuntimeException("not implemented");
    }

    /**
     * @return Collection of valid Pawn moves
     */
    public Collection<ChessMove> PawnMoveCalculator(ChessBoard board, ChessPosition MyPosition) {
        throw new RuntimeException("not implemented");
    }
}
