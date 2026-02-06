package net.fabledrealms.fr.player;

import com.hypixel.hytale.server.core.entity.entities.Player;
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

        File playerDataDir = new File(
                plugin.getDataDirectory(),
                "FabledRealms" + File.separator + "player_data"
        );

        this.playerJsonStore = new PlayerJsonStore(playerDataDir);
    }

    public FabledPlayer loadPlayer(Player player) {
        PlayerEcsData data = playerJsonStore.loadOrCreate(player.getUuid());
        FabledPlayer fabledPlayer = new FabledPlayer(player, data);

        onlinePlayers.put(player.getUuid(), fabledPlayer);

        plugin.getLogger().atInfo().log(
                "Loaded player %s (%s) with %s character(s).",
                player.getDisplayName(),
                player.getUuid(),
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
}
