package websocket.messages;

public class LoadGameMessage extends ServerMessage {
    private final String game;

    public LoadGameMessage(String game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public String getNewGame() {
        return game;
    }

    @Override
    public String toString() {
        return "LoadGame{" +
                "game='" + game + '\'' +
                '}';
    }
}
