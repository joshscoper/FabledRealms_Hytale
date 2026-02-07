package net.fabledrealms.fr.lifecycle;

import com.hypixel.hytale.event.EventRegistration;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.logger.HytaleLogger;
import net.fabledrealms.fr.player.FabledPlayerManager;

import java.util.UUID;

/**
 * API-native player lifecycle registration wrapper.
 */
public final class PlayerLifecycleModule implements AutoCloseable {

    private final HytaleLogger logger;
    private final FabledPlayerManager playerManager;

    private EventRegistration<Void, PlayerConnectEvent> connectRegistration;
    private EventRegistration<Void, PlayerDisconnectEvent> disconnectRegistration;

    public PlayerLifecycleModule(HytaleLogger logger, FabledPlayerManager playerManager) {
        this.logger = logger;
        this.playerManager = playerManager;
    }

    public void register(EventRegistry events) {
        connectRegistration = events.register(PlayerConnectEvent.class, this::onPlayerConnect);
        disconnectRegistration = events.register(PlayerDisconnectEvent.class, this::onPlayerDisconnect);
        logger.atInfo().log("Player lifecycle module registered.");
    }

    private void onPlayerConnect(PlayerConnectEvent event) {
        UUID playerId = event.getPlayerRef().getUuid();
        playerManager.loadPlayer(playerId, event.getPlayerRef(), null);
    }

    private void onPlayerDisconnect(PlayerDisconnectEvent event) {
        UUID playerId = event.getPlayerRef().getUuid();
        playerManager.unloadPlayer(playerId);
    }

    @Override
    public void close() {
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
}
