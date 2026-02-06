package net.fabledrealms.fr.player;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import net.fabledrealms.fr.player.data.PlayerEcsData;

import java.util.UUID;

/**
 * Runtime wrapper for online players.
 *
 * <p>Holds references to Hytale runtime player objects plus the persistent
 * ECS-style data graph that we serialize to JSON.</p>
 */
public final class FabledPlayer {

    private final Player player;
    private final PlayerRef playerRef;
    private final UUID playerId;
    private final PlayerEcsData ecsData;

    public FabledPlayer(Player player, PlayerEcsData ecsData) {
        this.player = player;
        this.playerRef = player.getPlayerRef();
        this.playerId = player.getUid();
        this.ecsData = ecsData;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerRef getPlayerRef() {
        return playerRef;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public PlayerEcsData getEcsData() {
        return ecsData;
    }
}
