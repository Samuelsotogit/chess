package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static chess.ChessGame.TeamColor.*;
import static ui.EscapeSequences.*;

public class PrintableBoard {
    private ChessGame game;
    private static Shade alternatinShade;

    PrintableBoard(ChessGame game) {
        this.game = game;
    }

    enum Shade {
        LIGHT,
        DARK
    }

    public String stringify(ChessGame.TeamColor side) {
        String[] rows = {FULL_1, FULL_2, FULL_3, FULL_4, FULL_5, FULL_6, FULL_7, FULL_8};
        String[] columns = {FULL_A, FULL_B, FULL_C, FULL_D, FULL_E, FULL_F, FULL_G, FULL_H};
        int start = 8;
        int end = 1;
        int increment = -1;
        if (side.equals(BLACK)) {
            start = 1;
            end = 8;
            increment = 1;
            rows = reverse(rows);
            columns = reverse(columns);
        }

        StringBuilder strBoard = new StringBuilder();

        strBoard.append(SET_BG_COLOR_WHITE);
        strBoard.append(EMPTY);
        for (String column : columns) {
            strBoard.append(column);
        }
        strBoard.append(EMPTY).append(RESET_BG_COLOR).append("\n");
        for (int row = start ; (end*increment - row*increment) >= 0 ; row+=increment) {
            String rowNumber = rows[(end*increment - row*increment)];
            strBoard.append(SET_BG_COLOR_WHITE).append(rowNumber).append(RESET_BG_COLOR);
            for (int col = end ; (col*increment - start*increment) >= 0; col-=increment) {
                alternatinShade = (alternatinShade == Shade.LIGHT) ? Shade.DARK : Shade.LIGHT;
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = game.getBoard().getPiece(position);
                strBoard.append(nextBackground(alternatinShade));
                if (piece != null) {
                    strBoard.append(pieceSymbol(piece));
                } else {
                    strBoard.append(EMPTY);
                }
                strBoard.append(RESET_BG_COLOR);

            }
            alternatinShade = (alternatinShade == Shade.LIGHT) ? Shade.DARK : Shade.LIGHT;
            strBoard.append(SET_BG_COLOR_WHITE);
            strBoard.append(rowNumber);
            strBoard.append(RESET_BG_COLOR);
            strBoard.append("\n");
        }
        strBoard.append(SET_BG_COLOR_WHITE);
        strBoard.append(EMPTY);
        for (String column : columns) {
            strBoard.append(column);
        }
        strBoard.append(EMPTY).append(RESET_BG_COLOR);
        return strBoard.toString();
    }

    public String pieceSymbol(ChessPiece piece) {
        return switch (piece.getPieceType()) {
            case KING -> piece.getTeamColor() == WHITE ? WHITE_KING : BLACK_KING;
            case QUEEN -> piece.getTeamColor() == WHITE ? WHITE_QUEEN : BLACK_QUEEN;
            case BISHOP -> piece.getTeamColor() == WHITE ? WHITE_BISHOP : BLACK_BISHOP;
            case KNIGHT -> piece.getTeamColor() == WHITE ? WHITE_KNIGHT : BLACK_KNIGHT;
            case ROOK -> piece.getTeamColor() == WHITE ? WHITE_ROOK : BLACK_ROOK;
            case PAWN -> piece.getTeamColor() == WHITE ? WHITE_PAWN : BLACK_PAWN;
        };
    }

    private String nextBackground(Shade shade) {
        if (shade == Shade.LIGHT) {
            return SET_BG_COLOR_LIGHT_WHITE;
        }
        else {
            return SET_BG_COLOR_DARK_RED;
        }
    }

    private String[] reverse(String[] stringArray) {
        ArrayList<String> newArray = new ArrayList<>(Arrays.asList(stringArray));
        Collections.reverse(newArray);
        return newArray.toArray(new String[0]);
    }
}
