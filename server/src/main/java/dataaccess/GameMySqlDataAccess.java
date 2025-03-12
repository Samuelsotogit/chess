package dataaccess;

import chess.ChessGame;
import data.transfer.objects.GamesListResponse;

import java.util.ArrayList;

public class GameMySqlDataAccess implements GameDAO {
    @Override
    public ArrayList<GamesListResponse> getGames() throws DataAccessException {
        return null;
    }

    @Override
    public int createGame(String gameName, Integer gameID) throws DataAccessException {
        return 0;
    }

    @Override
    public GamesListResponse getGame(Integer gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(Integer gameID, GamesListResponse game, ChessGame.TeamColor playerColor, String username) throws DataAccessException {

    }

    @Override
    public void deleteGames() throws DataAccessException {

    }
}
