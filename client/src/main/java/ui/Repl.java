package ui;

import chess.ChessGame;
import com.google.gson.JsonObject;
import model.AuthData;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    private final ChessClient client;
    private State state = State.SIGNEDOUT;
    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
    }

    public void run() {
        System.out.println(WHITE_KING + "Welcome to the best chess game ever. Type Help to view commands.");
        Scanner scanner = new Scanner(System.in);
        Object result = "";
        while (!result.toString().equals("quit")) {
            printPrompt(state);
            String line = scanner.nextLine();
            try {
                result = client.eval(line);
                if (result instanceof AuthData authData) {
                    state = State.SIGNEDIN;
                    System.out.println("Logged in as: " + authData.username());
                    continue;
                } else if (result instanceof String str) {
                    if (str.equals("Logged out")) {
                        state = State.SIGNEDOUT;
                    } else if (str.equals("quit")) {
                        state = State.SIGNEDOUT;
                        break;
                    }
                    // Figure out how to draw the board
                } else if (result instanceof JsonObject) {
                    System.out.println(result);
                }
                System.out.println(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }

    private void printPrompt(State state) {
        switch (state) {
            case SIGNEDOUT -> System.out.print(SET_TEXT_UNDERLINE + SET_TEXT_COLOR_GREEN +
                    "[LOGGED_OUT]" + RESET_TEXT_UNDERLINE + " >>> " + RESET_TEXT_COLOR);
            case SIGNEDIN -> System.out.print(SET_TEXT_UNDERLINE + SET_TEXT_COLOR_GREEN +
                    "[LOGGED_in]" + RESET_TEXT_UNDERLINE + " >>> " + RESET_TEXT_COLOR);
        }
    }
}