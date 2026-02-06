package net.fabledrealms.fr.player.data;

import net.fabledrealms.fr.player.data.model.CharacterProfile;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Player aggregate root with ECS-style character entities.
 */
public class PlayerEcsData {

    private UUID playerId;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    private UUID activeCharacterId;
    private Map<UUID, CharacterProfile> characters = new LinkedHashMap<>();

    public static PlayerEcsData createDefault(UUID playerId) {
        PlayerEcsData data = new PlayerEcsData();
        data.setPlayerId(playerId);

        CharacterProfile character = CharacterProfile.createDefault();
        data.getCharacters().put(character.getCharacterId(), character);
        data.setActiveCharacterId(character.getCharacterId());
        return data;
    }

    public CharacterProfile getActiveCharacter() {
        if (activeCharacterId == null) {
            return null;
        }
        return characters.get(activeCharacterId);
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UUID getActiveCharacterId() {
        return activeCharacterId;
    }

    public void setActiveCharacterId(UUID activeCharacterId) {
        this.activeCharacterId = activeCharacterId;
    }

    public Map<UUID, CharacterProfile> getCharacters() {
        return characters;
    }

    public void setCharacters(Map<UUID, CharacterProfile> characters) {
        this.characters = characters;
    }
}
