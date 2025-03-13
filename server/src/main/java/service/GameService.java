package service;

import chess.ChessGame;
import data.transfer.objects.GameRequest;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import server.ResponseException;
import model.GameID;

import java.util.Collection;
import java.util.HashMap;

public class GameService {

    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

    public GameService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public Collection<GameData> listGames(String authToken) throws ResponseException {
        try {
            if (authToken == null || authDAO.getAuthData(authToken) == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }
            return gameDAO.getGames();
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Error: Internal Server Error");
        }
    }

    public GameID createGame(GameRequest gameRequest) throws ResponseException {
        try {
            if (gameRequest.gameName() == null) {
                throw new ResponseException(400, "Error: bad request");
            }
            if (authDAO.getAuthData(gameRequest.authToken()) == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }
            return new GameID(gameDAO.createGame(null, null, gameRequest.gameName(), new ChessGame()));
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
            GameData game = gameDAO.getGame(gameID);
            if (game == null || playerColor == null) {
                throw new ResponseException(400, "Error: bad request");
            }
            if (playerColor.equals(ChessGame.TeamColor.WHITE) && game.whiteUsername() != null || playerColor.equals(ChessGame.TeamColor.BLACK) && game.blackUsername() != null) {
                throw new ResponseException(403, "Error: Forbidden");
            }
            gameDAO.updateGame(gameID,
                    playerColor.equals(ChessGame.TeamColor.WHITE) ? username : game.whiteUsername(),
                    playerColor.equals(ChessGame.TeamColor.BLACK) ? username : game.blackUsername(),
                    game.gameName(),
                    game.game());
        } catch (DataAccessException e) {
            throw new ResponseException(403, "Error: Internal Server Error");
        }
    }
}
