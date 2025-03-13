package dataaccess;

import chess.ChessGame;
import data.transfer.objects.GameRequest;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public interface GameDAO {
    Collection<GameData> getGames() throws DataAccessException;
    int createGame(String gameName, ChessGame chessGame) throws DataAccessException;
    GameData getGame(Integer gameID) throws DataAccessException;
    void updateGame(Integer gameID, String whiteUsername, String blackUsername, String gameName, ChessGame chessGame) throws DataAccessException;
    void deleteGames() throws DataAccessException;
}