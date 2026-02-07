package net.fabledrealms.fr.session;

import com.hypixel.hytale.server.core.universe.PlayerRef;

import java.util.UUID;

public final class PlayerSession {

    public enum State {
        CONNECTED,
        CHARACTER_MENU,
        IN_WORLD
    }

    private final UUID playerId;
    private final PlayerRef playerRef;
    private volatile State state;

    public PlayerSession(UUID playerId, PlayerRef playerRef) {
        this.playerId = playerId;
        this.playerRef = playerRef;
        this.state = State.CONNECTED;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public PlayerRef getPlayerRef() {
        return playerRef;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
