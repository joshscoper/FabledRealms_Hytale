package net.fabledrealms.fr.player;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import net.fabledrealms.fr.FabledRealmsPlugin;
import net.fabledrealms.fr.player.data.PlayerEcsData;
import net.fabledrealms.fr.player.data.PlayerJsonStore;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class FabledPlayerManager {

    private final FabledRealmsPlugin plugin;
    private final PlayerJsonStore playerJsonStore;
    private final Map<UUID, FabledPlayer> onlinePlayers = new ConcurrentHashMap<>();

    public FabledPlayerManager(FabledRealmsPlugin plugin) {
        this.plugin = plugin;

        File playerDataDir = plugin.getDataDirectory()
                .resolve("FabledRealms")
                .resolve("player_data")
                .toFile();

        this.playerJsonStore = new PlayerJsonStore(playerDataDir);
    }

    public FabledPlayer loadPlayer(Player player) {
        return loadPlayer(player.getUid(), player.getPlayerRef(), player.getDisplayName());
    }

    public FabledPlayer loadPlayer(UUID playerId, PlayerRef playerRef, String displayName) {
        PlayerEcsData data = playerJsonStore.loadOrCreate(playerId);
        data.setLastKnownName(displayName);

        FabledPlayer fabledPlayer = new FabledPlayer(playerId, playerRef, data);
        onlinePlayers.put(playerId, fabledPlayer);

        plugin.getLogger().atInfo().log(
                "Loaded player %s (%s) with %s character(s).",
                displayName,
                playerId,
                data.getCharacters().size()
        );

        return fabledPlayer;
    }

    public void savePlayer(UUID playerId) {
        FabledPlayer fabledPlayer = onlinePlayers.get(playerId);
        if (fabledPlayer == null) {
            return;
        }

        playerJsonStore.save(fabledPlayer.getEcsData());
    }

    public void unloadPlayer(UUID playerId) {
        FabledPlayer fabledPlayer = onlinePlayers.remove(playerId);
        if (fabledPlayer == null) {
            return;
        }

        playerJsonStore.save(fabledPlayer.getEcsData());
    }

    public void saveAll() {
        onlinePlayers.values().forEach(fp -> playerJsonStore.save(fp.getEcsData()));
    }

    public Optional<FabledPlayer> getPlayer(UUID playerId) {
        return Optional.ofNullable(onlinePlayers.get(playerId));
    }

    public Optional<PlayerEcsData> getPlayerData(UUID playerId) {
        FabledPlayer online = onlinePlayers.get(playerId);
        if (online != null) {
            return Optional.of(online.getEcsData());
        }
        return Optional.of(playerJsonStore.loadOrCreate(playerId));
    }

    public Optional<PlayerEcsData> getPlayerDataByName(String playerName) {
        Optional<FabledPlayer> online = onlinePlayers.values().stream()
                .filter(fp -> fp.getEcsData().getLastKnownName().equalsIgnoreCase(playerName))
                .findFirst();

        if (online.isPresent()) {
            return Optional.of(online.get().getEcsData());
        }

        return playerJsonStore.findByName(playerName);
    }
}
