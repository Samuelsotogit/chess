package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    ArrayList<GameData> getGames(String authToken) throws DataAccessException;
    int createGame(String gameName, String gameID) throws DataAccessException;
    GameData getGame(Integer gameID) throws DataAccessException;
    boolean updateGame(Integer gameID, GameData game, ChessGame.TeamColor playerColor, String username) throws DataAccessException;
}
