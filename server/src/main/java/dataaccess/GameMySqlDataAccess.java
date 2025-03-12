package dataaccess;

import chess.ChessGame;
import data.transfer.objects.GamesListResponse;
import server.ResponseException;

import java.util.ArrayList;

public class GameMySqlDataAccess implements GameDAO {

    DatabaseManager databaseManager = new DatabaseManager();

    public GameMySqlDataAccess() {
        try {
            String[] createGameTable = {
                    """
            CREATE TABLE IF NOT EXISTS  games (
              `gameID` INT NOT NULL UNIQUE,
              `whiteUsername` VARCHAR(100),
              `blackUsername` VARCHAR(100),
              `gameName` VARCHAR(100) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
            };
            databaseManager.configureDatabase(createGameTable);
        } catch (ResponseException | DataAccessException exception) {
            System.out.println("Data Access Error");
        }
    }

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
