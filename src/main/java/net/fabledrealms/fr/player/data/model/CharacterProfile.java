package net.fabledrealms.fr.player.data.model;

import net.fabledrealms.fr.player.data.component.EconomyComponent;
import net.fabledrealms.fr.player.data.component.PlayerStatsComponent;
import net.fabledrealms.fr.player.data.component.ProfessionsComponent;
import net.fabledrealms.fr.player.data.component.SocialComponent;

import java.time.Instant;
import java.util.UUID;

/**
 * Character entity with attached components.
 */
public class CharacterProfile {

    private UUID characterId;
    private String characterName = "Adventurer";
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    private PlayerStatsComponent stats = new PlayerStatsComponent();
    private EconomyComponent economy = new EconomyComponent();
    private SocialComponent social = new SocialComponent();
    private ProfessionsComponent professions = new ProfessionsComponent();

    public static CharacterProfile createDefault() {
        CharacterProfile profile = new CharacterProfile();
        profile.setCharacterId(UUID.randomUUID());
        return profile;
    }

    public UUID getCharacterId() {
        return characterId;
    }

    public void setCharacterId(UUID characterId) {
        this.characterId = characterId;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
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

    public PlayerStatsComponent getStats() {
        return stats;
    }

    public void setStats(PlayerStatsComponent stats) {
        this.stats = stats;
    }

    public EconomyComponent getEconomy() {
        return economy;
    }

    public void setEconomy(EconomyComponent economy) {
        this.economy = economy;
    }

    public SocialComponent getSocial() {
        return social;
    }

    public void setSocial(SocialComponent social) {
        this.social = social;
    }

    public ProfessionsComponent getProfessions() {
        return professions;
    }

    public void setProfessions(ProfessionsComponent professions) {
        this.professions = professions;
    }
}
