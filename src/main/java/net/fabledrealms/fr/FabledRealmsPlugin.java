package net.fabledrealms.fr;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import net.fabledrealms.fr.player.FabledPlayerManager;

import javax.annotation.Nonnull;

public final class FabledRealmsPlugin extends JavaPlugin {

    private FabledPlayerManager playerManager;

    public FabledRealmsPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    public void start() {
        this.playerManager = new FabledPlayerManager(this);
        getLogger().atInfo().log("FabledRealms started with JSON ECS player persistence enabled.");

        // TODO: Register join/quit listeners and call playerManager.loadPlayer/unloadPlayer.
    }

    @Override
    public void shutdown() {
        if (playerManager != null) {
            playerManager.saveAll();
        }

        getLogger().atInfo().log("FabledRealms shutdown complete. Player data flushed to JSON.");
    }

    public FabledPlayerManager getPlayerManager() {
        return playerManager;
    }
}
