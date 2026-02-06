package net.fabledrealms.fr;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import net.fabledrealms.fr.admin.FabledAdminCommandService;
import net.fabledrealms.fr.player.FabledPlayerManager;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public final class FabledRealmsPlugin extends JavaPlugin {

    private FabledPlayerManager playerManager;
    private FabledAdminCommandService adminCommandService;

    public FabledRealmsPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    public void start() {
        this.playerManager = new FabledPlayerManager(this);
        this.adminCommandService = new FabledAdminCommandService(playerManager);
        getLogger().atInfo().log("FabledRealms started with JSON ECS player persistence enabled.");

        // TODO: Register join/quit listeners and call playerManager.loadPlayer/unloadPlayer.
        // TODO: Register /fradmin inspect <playerName|uuid> and forward to runAdminInspectCommand.
    }

    @Override
    public void shutdown() {
        if (playerManager != null) {
            playerManager.saveAll();
        }

        getLogger().atInfo().log("FabledRealms shutdown complete. Player data flushed to JSON.");
    }

    public void runAdminInspectCommand(String target, Consumer<String> reply) {
        adminCommandService.inspectPlayerCharacters(target, reply);
    }

    public FabledPlayerManager getPlayerManager() {
        return playerManager;
    }

    public FabledAdminCommandService getAdminCommandService() {
        return adminCommandService;
    }
}
