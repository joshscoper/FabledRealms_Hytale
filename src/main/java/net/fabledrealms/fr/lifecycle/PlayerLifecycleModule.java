package net.fabledrealms.fr.lifecycle;

import com.hypixel.hytale.event.EventRegistration;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import net.fabledrealms.fr.player.FabledPlayerManager;
import net.fabledrealms.fr.session.PlayerSession;
import net.fabledrealms.fr.session.SessionStore;
import net.fabledrealms.fr.ui.JoinCharacterMenuPrompt;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * API-native player lifecycle registration wrapper.
 */
public final class PlayerLifecycleModule implements AutoCloseable {

    private final HytaleLogger logger;
    private final FabledPlayerManager playerManager;
    private final SessionStore sessionStore;
    private final JoinCharacterMenuPrompt joinPrompt;

    private EventRegistration<Void, PlayerConnectEvent> connectRegistration;
    private EventRegistration<Void, PlayerDisconnectEvent> disconnectRegistration;

    public PlayerLifecycleModule(HytaleLogger logger,
                                 FabledPlayerManager playerManager,
                                 SessionStore sessionStore,
                                 JoinCharacterMenuPrompt joinPrompt) {
        this.logger = logger;
        this.playerManager = playerManager;
        this.sessionStore = sessionStore;
        this.joinPrompt = joinPrompt;
    }

    public void register(EventRegistry events) {
        connectRegistration = events.register(PlayerConnectEvent.class, this::onPlayerConnect);
        disconnectRegistration = events.register(PlayerDisconnectEvent.class, this::onPlayerDisconnect);
        logger.atInfo().log("Player lifecycle module registered.");
    }

    private void onPlayerConnect(PlayerConnectEvent event) {
        UUID playerId = event.getPlayerRef().getUuid();
        String displayName = resolveDisplayName(event.getPlayerRef());
        playerManager.loadPlayer(playerId, event.getPlayerRef(), displayName);

        PlayerSession session = new PlayerSession(playerId, event.getPlayerRef());
        session.setState(PlayerSession.State.CHARACTER_MENU);
        sessionStore.put(session);

        if (!joinPrompt.trySendPrompt(event.getPlayerRef())) {
            logger.atInfo().log("Join prompt could not be pushed directly for %s. Use /chars.", playerId);
        }
    }

    private void onPlayerDisconnect(PlayerDisconnectEvent event) {
        UUID playerId = event.getPlayerRef().getUuid();
        sessionStore.remove(event.getPlayerRef());
        playerManager.unloadPlayer(playerId);
    }

    private String resolveDisplayName(PlayerRef playerRef) {
        for (String methodName : new String[]{"getDisplayName", "getName", "getUsername"}) {
            try {
                Method method = playerRef.getClass().getMethod(methodName);
                Object value = method.invoke(playerRef);
                if (value instanceof String text && !text.isBlank()) {
                    return text;
                }
            } catch (Exception ignored) {
            }
        }
        return null;
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

        sessionStore.clear();
    }
}
