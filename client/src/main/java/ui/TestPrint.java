package ui;

import chess.ChessGame;

public class TestPrint {
    public static void main(String[] args) {
        PrintableBoard board = new PrintableBoard(new ChessGame());
        System.out.println(board.stringify(ChessGame.TeamColor.BLACK));
    }
}
