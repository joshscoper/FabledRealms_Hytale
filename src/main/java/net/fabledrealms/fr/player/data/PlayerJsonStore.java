package net.fabledrealms.fr.player.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.fabledrealms.fr.player.data.component.EconomyComponent;
import net.fabledrealms.fr.player.data.component.PlayerStatsComponent;
import net.fabledrealms.fr.player.data.component.ProfessionsComponent;
import net.fabledrealms.fr.player.data.component.SocialComponent;
import net.fabledrealms.fr.player.data.model.CharacterProfile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
        Optional<PlayerEcsData> loaded = load(playerId);
        if (loaded.isPresent()) {
            return loaded.get();
        }

        PlayerEcsData created = normalize(PlayerEcsData.createDefault(playerId), playerId);
        save(created);
        return created;
    }

    public Optional<PlayerEcsData> load(UUID playerId) {
        File file = resolveFile(playerId);
        if (!file.exists()) {
            return Optional.empty();
        }

        try {
            PlayerEcsData data = mapper.readValue(file, PlayerEcsData.class);
            return Optional.of(normalize(data, playerId));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read player JSON: " + file.getAbsolutePath(), e);
        }
    }

    public void save(PlayerEcsData data) {
        if (data.getPlayerId() == null) {
            throw new IllegalArgumentException("Player ID is required before saving.");
        }

        PlayerEcsData normalized = normalize(data, data.getPlayerId());
        normalized.setUpdatedAt(Instant.now());

        File file = resolveFile(normalized.getPlayerId());
        File tmpFile = new File(file.getParentFile(), file.getName() + ".tmp");
        try {
            Files.createDirectories(playerDataDir.toPath());
            mapper.writeValue(tmpFile, normalized);
            Files.move(tmpFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
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
                    results.add(normalize(data, data.getPlayerId()));
                }
            } catch (IOException ignored) {
            }
        }

        return results;
    }

    private PlayerEcsData normalize(PlayerEcsData data, UUID fallbackPlayerId) {
        if (data.getPlayerId() == null) data.setPlayerId(fallbackPlayerId);
        if (data.getLastKnownName() == null || data.getLastKnownName().isBlank()) data.setLastKnownName("Unknown");
        if (data.getCharacters() == null) data.setCharacters(new LinkedHashMap<>());

        if (data.getCharacters().isEmpty()) {
            CharacterProfile profile = CharacterProfile.createDefault();
            data.getCharacters().put(profile.getCharacterId(), profile);
            data.setActiveCharacterId(profile.getCharacterId());
        }

        Map<UUID, CharacterProfile> fixed = new LinkedHashMap<>();
        for (Map.Entry<UUID, CharacterProfile> entry : data.getCharacters().entrySet()) {
            UUID id = entry.getKey();
            CharacterProfile profile = entry.getValue() != null ? entry.getValue() : CharacterProfile.createDefault();
            if (profile.getCharacterId() == null) profile.setCharacterId(id != null ? id : UUID.randomUUID());
            if (profile.getCharacterName() == null || profile.getCharacterName().isBlank()) profile.setCharacterName("Adventurer");
            if (profile.getStats() == null) profile.setStats(new PlayerStatsComponent());
            if (profile.getEconomy() == null) profile.setEconomy(new EconomyComponent());
            if (profile.getSocial() == null) profile.setSocial(new SocialComponent());
            if (profile.getProfessions() == null) profile.setProfessions(new ProfessionsComponent());
            fixed.put(profile.getCharacterId(), profile);
        }
        data.setCharacters(fixed);

        if (data.getActiveCharacterId() == null || !data.getCharacters().containsKey(data.getActiveCharacterId())) {
            data.setActiveCharacterId(data.getCharacters().keySet().iterator().next());
        }
        return data;
    }

    private File resolveFile(UUID playerId) {
        return new File(playerDataDir, playerId + ".json");
    }
}
