package dataaccess;

import DataTransferObjects.GamesListResponse;
import chess.ChessGame;

import java.util.ArrayList;

public interface GameDAO {
    ArrayList<GamesListResponse> getGames() throws DataAccessException;
    int createGame(String gameName, Integer gameID) throws DataAccessException;
    GamesListResponse getGame(Integer gameID) throws DataAccessException;
    void updateGame(Integer gameID, GamesListResponse game, ChessGame.TeamColor playerColor, String username) throws DataAccessException;
    void deleteGames() throws DataAccessException;
}
