package dataaccess;

import chess.ChessGame;
import model.GameData;
import java.util.Collection;

public interface GameDAO {
    Collection<GameData> getGames() throws DataAccessException;
    int createGame(String gameName, ChessGame chessGame) throws DataAccessException;
    GameData getGame(Integer gameID) throws DataAccessException;
    void updateGame(Integer gameID, String whiteUsername, String blackUsername, String gameName, ChessGame chessGame) throws DataAccessException;
    void deleteGames() throws DataAccessException;
}