package net.fabledrealms.fr.player.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Lightweight JSON persistence for player ECS data.
 */
public final class PlayerJsonStore {

    private final File playerDataDir;
    private final ObjectMapper mapper;

    public PlayerJsonStore(File playerDataDir) {
        this.playerDataDir = playerDataDir;
        this.mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(SerializationFeature.INDENT_OUTPUT);
    }

    public PlayerEcsData loadOrCreate(UUID playerId) {
        File file = resolveFile(playerId);

        if (!file.exists()) {
            PlayerEcsData created = PlayerEcsData.createDefault(playerId);
            save(created);
            return created;
        }

        try {
            PlayerEcsData data = mapper.readValue(file, PlayerEcsData.class);
            if (data.getPlayerId() == null) {
                data.setPlayerId(playerId);
            }
            if (data.getLastKnownName() == null || data.getLastKnownName().isBlank()) {
                data.setLastKnownName("Unknown");
            }
            return data;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read player JSON: " + file.getAbsolutePath(), e);
        }
    }

    public void save(PlayerEcsData data) {
        if (data.getPlayerId() == null) {
            throw new IllegalArgumentException("Player ID is required before saving.");
        }

        data.setUpdatedAt(Instant.now());

        File file = resolveFile(data.getPlayerId());
        try {
            Files.createDirectories(playerDataDir.toPath());
            mapper.writeValue(file, data);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write player JSON: " + file.getAbsolutePath(), e);
        }
    }

    public Optional<PlayerEcsData> findByName(String playerName) {
        return loadAll().stream()
                .filter(data -> data.getLastKnownName() != null)
                .filter(data -> data.getLastKnownName().equalsIgnoreCase(playerName))
                .findFirst();
    }

    public List<PlayerEcsData> loadAll() {
        List<PlayerEcsData> results = new ArrayList<>();
        if (!playerDataDir.exists() || !playerDataDir.isDirectory()) {
            return results;
        }

        File[] files = playerDataDir.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null) {
            return results;
        }

        for (File file : files) {
            try {
                PlayerEcsData data = mapper.readValue(file, PlayerEcsData.class);
                if (data.getPlayerId() != null) {
                    results.add(data);
                }
            } catch (IOException ignored) {
                // skip malformed files but keep the admin command functional for valid entries
            }
        }

        return results;
    }

    private File resolveFile(UUID playerId) {
        return new File(playerDataDir, playerId + ".json");
    }
}
