package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class GameMemoryDataAccess implements GameDAO {
    ArrayList<GameData> games = new ArrayList<>();
    private int nextGameId = 1;

    @Override
    public ArrayList<GameData> getGames(String authToken) throws DataAccessException {
        return games;
    }

    @Override
    public int createGame(String gameName, String gameID) throws DataAccessException {
        GameData game = new GameData(nextGameId++, null, null, gameName, null);
        games.add(game);
        return game.gameID();
    }

    @Override
    public GameData getGame(Integer gameID) throws DataAccessException {
        for (GameData game : games) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    @Override
    public boolean updateGame(Integer gameID, GameData game, ChessGame.TeamColor playerColor, String username) throws DataAccessException {
        if (playerColor.equals(ChessGame.TeamColor.WHITE)) {
            GameData newGame = new GameData(gameID, username, null, game.gameName(), new ChessGame());
            games.remove(getGame(gameID));
            games.add(newGame);
            return true;
        } else if (playerColor.equals(ChessGame.TeamColor.BLACK)) {
            GameData newGame = new GameData(gameID, null, username, game.gameName(), new ChessGame());
            games.remove(getGame(gameID));
            games.add(newGame);
            return true;
        }
        return false;
    }
}
