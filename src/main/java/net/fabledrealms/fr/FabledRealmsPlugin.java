package net.fabledrealms.fr;

import com.hypixel.hytale.event.EventRegistration;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import net.fabledrealms.fr.admin.FabledAdminCommandService;
import net.fabledrealms.fr.command.FRAdminInspectCommand;
import net.fabledrealms.fr.player.FabledPlayerManager;

import javax.annotation.Nonnull;
import java.util.UUID;
import java.util.function.Consumer;

public final class FabledRealmsPlugin extends JavaPlugin {

    private FabledPlayerManager playerManager;
    private FabledAdminCommandService adminCommandService;

    private EventRegistration<Void, PlayerConnectEvent> connectRegistration;
    private EventRegistration<Void, PlayerDisconnectEvent> disconnectRegistration;

    public FabledRealmsPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    public void start() {
        this.playerManager = new FabledPlayerManager(this);
        this.adminCommandService = new FabledAdminCommandService(playerManager);

        registerLifecycleEvents();
        registerCommands();

        getLogger().atInfo().log("FabledRealms started with API-native player lifecycle and admin command wiring.");
    }

    @Override
    public void shutdown() {
        unregisterLifecycleEvents();

        if (playerManager != null) {
            playerManager.saveAll();
        }

        getLogger().atInfo().log("FabledRealms shutdown complete. Player data flushed to JSON.");
    }

    private void registerCommands() {
        getCommandRegistry().registerCommand(new FRAdminInspectCommand(this));
    }

    private void registerLifecycleEvents() {
        connectRegistration = getEventRegistry().register(PlayerConnectEvent.class, this::onPlayerConnect);
        disconnectRegistration = getEventRegistry().register(PlayerDisconnectEvent.class, this::onPlayerDisconnect);
    }

    private void unregisterLifecycleEvents() {
        try {
            if (connectRegistration != null) {
                connectRegistration.unregister();
            }
        } catch (Exception ignored) {
        }

        try {
            if (disconnectRegistration != null) {
                disconnectRegistration.unregister();
            }
        } catch (Exception ignored) {
        }
    }

    private void onPlayerConnect(PlayerConnectEvent event) {
        UUID playerId = event.getPlayerRef().getUuid();
        String displayName = event.getPlayerRef().toString();
        playerManager.loadPlayer(playerId, event.getPlayerRef(), displayName);
    }

    private void onPlayerDisconnect(PlayerDisconnectEvent event) {
        UUID playerId = event.getPlayerRef().getUuid();
        playerManager.unloadPlayer(playerId);
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
