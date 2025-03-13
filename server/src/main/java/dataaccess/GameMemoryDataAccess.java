package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class GameMemoryDataAccess implements GameDAO {
    HashMap<Integer, GameData> games = new HashMap<>();
    int nextID;

    public GameMemoryDataAccess() {
        nextID = 1;
    }

    @Override
    public int createGame(String whiteUsername, String blackUsername, String gameName, ChessGame chessGame) throws DataAccessException {
        GameData game = new GameData(nextID, null, null, gameName, chessGame);
        games.put(nextID++ ,game);
        return game.gameID();
    }

    @Override
    public GameData getGame(Integer gameID) throws DataAccessException {
        return games.get(gameID);
    }

    @Override
    public Collection<GameData> getGames() throws DataAccessException {
        return games.values();
    }

    @Override
    public void updateGame(Integer gameID, String whiteUsername, String blackUsername, String gameName, ChessGame chessGame) throws DataAccessException {
        GameData game = new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
        games.put(gameID ,game);
    }

    public void deleteGames() throws DataAccessException {
        games.clear();
    }
}
