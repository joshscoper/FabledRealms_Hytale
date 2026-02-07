package net.fabledrealms.fr.player;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import net.fabledrealms.fr.FabledRealmsPlugin;
import net.fabledrealms.fr.player.data.PlayerEcsData;
import net.fabledrealms.fr.player.data.PlayerJsonStore;
import net.fabledrealms.fr.player.data.model.CharacterProfile;
import net.fabledrealms.fr.player.model.CharacterSummary;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class FabledPlayerManager {

    private static final int MAX_CHARACTERS = 6;

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
        return loadPlayer(player.getPlayerRef().getUuid(), player.getPlayerRef(), player.getDisplayName());
    }

    public FabledPlayer loadPlayer(UUID playerId, PlayerRef playerRef, String displayName) {
        Optional<PlayerEcsData> existing = playerJsonStore.load(playerId);
        boolean isNew = existing.isEmpty();

        PlayerEcsData data = existing.orElseGet(() -> PlayerEcsData.createDefault(playerId));

        if (isUsefulDisplayName(displayName)) {
            data.setLastKnownName(displayName);

            if (isNew) {
                CharacterProfile active = data.getActiveCharacter();
                if (active != null && (active.getCharacterName() == null || active.getCharacterName().isBlank()
                        || "Adventurer".equalsIgnoreCase(active.getCharacterName()))) {
                    active.setCharacterName(displayName);
                }
            }
        }

        FabledPlayer fabledPlayer = new FabledPlayer(playerId, playerRef, data);
        onlinePlayers.put(playerId, fabledPlayer);

        playerJsonStore.save(data);

        plugin.getLogger().atInfo().log(
                "Loaded player %s (%s) with %s character(s).",
                data.getLastKnownName(),
                playerId,
                data.getCharacters().size()
        );

        return fabledPlayer;
    }

    public List<CharacterSummary> getRoster(UUID playerId) {
        PlayerEcsData data = resolveData(playerId).orElseThrow();
        List<CharacterSummary> roster = new ArrayList<>();

        data.getCharacters().forEach((characterId, character) -> roster.add(
                new CharacterSummary(
                        characterId,
                        character.getCharacterName(),
                        character.getStats().getLevel(),
                        characterId.equals(data.getActiveCharacterId())
                )
        ));

        return roster;
    }

    public CharacterSummary createCharacter(UUID playerId, String name) {
        PlayerEcsData data = resolveData(playerId).orElseThrow();

        if (data.getCharacters().size() >= MAX_CHARACTERS) {
            throw new IllegalStateException("Character limit reached (" + MAX_CHARACTERS + ").");
        }

        String cleanName = sanitizeName(name);
        boolean exists = data.getCharacters().values().stream()
                .anyMatch(c -> c.getCharacterName().equalsIgnoreCase(cleanName));
        if (exists) {
            throw new IllegalStateException("Character name already exists.");
        }

        CharacterProfile created = CharacterProfile.createDefault();
        created.setCharacterName(cleanName);
        data.getCharacters().put(created.getCharacterId(), created);

        if (data.getActiveCharacterId() == null) {
            data.setActiveCharacterId(created.getCharacterId());
        }

        persistLoaded(playerId, data);

        return new CharacterSummary(created.getCharacterId(), created.getCharacterName(), created.getStats().getLevel(),
                created.getCharacterId().equals(data.getActiveCharacterId()));
    }

    public CharacterSummary selectCharacterByIndex(UUID playerId, int index) {
        PlayerEcsData data = resolveData(playerId).orElseThrow();
        List<CharacterSummary> roster = getRoster(playerId);
        if (index < 0 || index >= roster.size()) {
            throw new IllegalStateException("Invalid index.");
        }

        CharacterSummary selected = roster.get(index);
        data.setActiveCharacterId(selected.characterId());
        persistLoaded(playerId, data);

        return new CharacterSummary(selected.characterId(), selected.name(), selected.level(), true);
    }

    public CharacterSummary deleteCharacterByIndex(UUID playerId, int index) {
        PlayerEcsData data = resolveData(playerId).orElseThrow();
        List<CharacterSummary> roster = getRoster(playerId);
        if (index < 0 || index >= roster.size()) {
            throw new IllegalStateException("Invalid index.");
        }
        if (roster.size() <= 1) {
            throw new IllegalStateException("You must keep at least one character.");
        }

        CharacterSummary deleted = roster.get(index);
        data.getCharacters().remove(deleted.characterId());

        if (deleted.characterId().equals(data.getActiveCharacterId())) {
            UUID newActive = data.getCharacters().keySet().iterator().next();
            data.setActiveCharacterId(newActive);
        }

        persistLoaded(playerId, data);
        return deleted;
    }

    private Optional<PlayerEcsData> resolveData(UUID playerId) {
        FabledPlayer online = onlinePlayers.get(playerId);
        if (online != null) {
            return Optional.of(online.getEcsData());
        }
        return playerJsonStore.load(playerId);
    }

    private void persistLoaded(UUID playerId, PlayerEcsData data) {
        onlinePlayers.computeIfPresent(playerId, (ignored, existing) -> {
            existing.getEcsData().setActiveCharacterId(data.getActiveCharacterId());
            existing.getEcsData().setCharacters(data.getCharacters());
            existing.getEcsData().setLastKnownName(data.getLastKnownName());
            return existing;
        });
        playerJsonStore.save(data);
    }

    private String sanitizeName(String name) {
        if (name == null) {
            throw new IllegalStateException("Name is required.");
        }

        String trimmed = name.trim();
        if (trimmed.length() < 3 || trimmed.length() > 16) {
            throw new IllegalStateException("Name must be 3-16 characters.");
        }

        if (!trimmed.matches("[A-Za-z0-9_]+")) {
            throw new IllegalStateException("Name must be alphanumeric/underscore only.");
        }

        return trimmed;
    }

    private boolean isUsefulDisplayName(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }

        try {
            UUID.fromString(value);
            return false;
        } catch (IllegalArgumentException ignored) {
            return true;
        }
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
        return playerJsonStore.load(playerId);
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
