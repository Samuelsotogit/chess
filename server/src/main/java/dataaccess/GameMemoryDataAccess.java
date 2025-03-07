package dataaccess;

import data.transfer.objects.GamesListResponse;
import chess.ChessGame;

import java.util.ArrayList;
import java.util.Objects;

public class GameMemoryDataAccess implements GameDAO {
    ArrayList<GamesListResponse> games = new ArrayList<>();
    int nextID;

    public GameMemoryDataAccess() {
        nextID = 1;
    }

    @Override
    public int createGame(String gameName, Integer gameID) throws DataAccessException {
        GamesListResponse game = new GamesListResponse(gameID+nextID++, null, null, gameName);
        games.add(game);
        return game.gameID();
    }

    @Override
    public GamesListResponse getGame(Integer gameID) throws DataAccessException {
        for (GamesListResponse game : games) {
            if (Objects.equals(game.gameID(), gameID)) {
                return game;
            }
        }
        return null;
    }

    @Override
    public ArrayList<GamesListResponse> getGames() throws DataAccessException {
        return games;
    }

    @Override
    public void updateGame(Integer gameID, GamesListResponse game, ChessGame.TeamColor playerColor, String username) throws DataAccessException {

        if (game.whiteUsername() == null && playerColor.equals(ChessGame.TeamColor.WHITE)) {
            GamesListResponse newGame = new GamesListResponse(gameID, username, game.blackUsername(), game.gameName());
            games.remove(getGame(gameID));
            games.add(newGame);
        } else if (game.blackUsername() == null && playerColor.equals(ChessGame.TeamColor.BLACK)) {
            GamesListResponse newGame = new GamesListResponse(gameID, game.whiteUsername(), username, game.gameName());
            games.remove(getGame(gameID));
            games.add(newGame);
        } else if (game.whiteUsername() != null && playerColor.equals(ChessGame.TeamColor.WHITE)) {
            throw new DataAccessException("color already taken");
        } else if (game.blackUsername() != null && playerColor.equals(ChessGame.TeamColor.BLACK)) {
            throw new DataAccessException("color already taken");
        }
    }

    public void deleteGames() throws DataAccessException {
        nextID = 1;
        games.clear();
    }
}
