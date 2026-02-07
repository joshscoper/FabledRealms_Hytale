package net.fabledrealms.fr.session;

import com.hypixel.hytale.server.core.universe.PlayerRef;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class SessionStore {

    private final Map<UUID, PlayerSession> byPlayerId = new ConcurrentHashMap<>();

    public void put(PlayerSession session) {
        byPlayerId.put(session.getPlayerId(), session);
    }

    public Optional<PlayerSession> get(PlayerRef playerRef) {
        return Optional.ofNullable(byPlayerId.get(playerRef.getUuid()));
    }

    public Optional<PlayerSession> get(UUID playerId) {
        return Optional.ofNullable(byPlayerId.get(playerId));
    }

    public void remove(PlayerRef playerRef) {
        byPlayerId.remove(playerRef.getUuid());
    }

    public void clear() {
        byPlayerId.clear();
    }
}
