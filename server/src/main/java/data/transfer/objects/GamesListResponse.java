package data.transfer.objects;

import java.util.Objects;

public record GamesListResponse(Integer gameID, String whiteUsername, String blackUsername, String gameName) {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GamesListResponse that = (GamesListResponse) o;
        return Objects.equals(gameID, that.gameID)
                && Objects.equals(gameName, that.gameName)
                && Objects.equals(whiteUsername, that.whiteUsername)
                && Objects.equals(blackUsername, that.blackUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, whiteUsername, blackUsername, gameName);
    }
}
