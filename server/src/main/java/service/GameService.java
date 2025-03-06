package service;

import DataTransferObjects.GamesListResponse;
import chess.ChessGame;
import dataaccess.AuthMemoryDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameMemoryDataAccess;
import dataaccess.UserMemoryDataAccess;
import model.AuthData;
import server.ResponseException;
import model.GameID;

import java.util.ArrayList;
import java.util.UUID;

public class GameService {

    UserMemoryDataAccess userDAO;
    AuthMemoryDataAccess authDAO;
    GameMemoryDataAccess gameDAO;

    public GameService(UserMemoryDataAccess userDAO, AuthMemoryDataAccess authDAO, GameMemoryDataAccess gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ArrayList<GamesListResponse> listGames(String authToken) throws ResponseException {
        try {
            if (authToken == null || authDAO.getAuthData(authToken) == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }
            return gameDAO.getGames();
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Error: Internal Server Error");
        }
    }

    public GameID createGame(String authToken, String gameName) throws ResponseException {
        try {
            if (gameName == null) {
                throw new ResponseException(400, "Error: bad request");
            }
            if (authDAO.getAuthData(authToken) == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }
            return new GameID(gameDAO.createGame(gameName, 1));
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Error: Internal Server Error");
        }
    }

    public void joinGame(String authToken, ChessGame.TeamColor playerColor, Integer gameID) throws ResponseException {
        try {
            AuthData authData = authDAO.getAuthData(authToken);
            if (authData == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }
            String username = authDAO.getAuthData(authToken).username();
            GamesListResponse game = gameDAO.getGame(gameID);
            if (game == null || playerColor == null) {
                throw new ResponseException(400, "Error: bad request");
            }
            if (game.whiteUsername() != null && game.blackUsername() != null) {
                throw new ResponseException(403, "Error: already taken");
            }
            gameDAO.updateGame(gameID, game, playerColor, username);
        } catch (DataAccessException e) {
            throw new ResponseException(403, "Error: Internal Server Error");
        }
    }

}
