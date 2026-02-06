package net.fabledrealms.fr.player;

import com.hypixel.hytale.server.core.entity.entities.Player;
import net.fabledrealms.fr.FabledRealmsPlugin;
import net.fabledrealms.fr.util.ResourceYamlUtil;

import java.io.File;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class FabledPlayerManager {

    private final FabledRealmsPlugin plugin;
    private final Map<UUID, FabledPlayer> players = new ConcurrentHashMap<>();

    public FabledPlayerManager(FabledRealmsPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadPlayer(Player player) {
        File dataFile = getPlayerDataFile(player);

        boolean existed = dataFile.exists();
        boolean ok = ensurePlayerDataFile(dataFile);

        if (!ok) {
            plugin.getLogger().atSevere().log("Failed to create player data file for %s (%s)",
                    player.getDisplayName(), player.getUuid());
            return;
        }

        if (!existed) {
            plugin.getLogger().atInfo().log("Created data file for player %s (%s)",
                    player.getDisplayName(), player.getUuid());
        }

        // Now load/store the player
        FabledPlayer fp = new FabledPlayer(player, dataFile);
        players.put(player.getUuid(), fp);
    }

    private File getPlayerDataFile(Player player) {
        return new File(
                plugin.getDataDirectory() + File.separator + "FabledRealms" + File.separator + "player_data",
                player.getUuid().toString() + ".yml"
        );
    }

    private boolean ensurePlayerDataFile(File file) {
        if (file.exists()) return true;

        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            return false;
        }

        try {
            ResourceYamlUtil.createYamlFromTemplate(
                    "templates/player_data_template.yml",
                    file.toPath(),
                    false
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
