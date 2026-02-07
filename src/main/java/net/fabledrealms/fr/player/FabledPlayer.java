package net.fabledrealms.fr.player;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import net.fabledrealms.fr.player.data.PlayerEcsData;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Runtime wrapper for online players.
 */
public final class FabledPlayer {

    private final UUID playerId;
    private final PlayerRef playerRef;
    private final PlayerEcsData ecsData;
    private final Player player;

    public FabledPlayer(Player player, PlayerEcsData ecsData) {
        this(player.getPlayerRef().getUuid(), player.getPlayerRef(), ecsData, player);
    }

    public FabledPlayer(UUID playerId, PlayerRef playerRef, PlayerEcsData ecsData) {
        this(playerId, playerRef, ecsData, null);
    }

    private FabledPlayer(UUID playerId, PlayerRef playerRef, PlayerEcsData ecsData, @Nullable Player player) {
        this.playerId = playerId;
        this.playerRef = playerRef;
        this.ecsData = ecsData;
        this.player = player;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public PlayerRef getPlayerRef() {
        return playerRef;
    }

    public PlayerEcsData getEcsData() {
        return ecsData;
    }

    @Nullable
    public Player getPlayer() {
        return player;
    }
}
