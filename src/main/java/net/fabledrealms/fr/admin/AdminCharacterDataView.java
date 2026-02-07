package net.fabledrealms.fr.admin;

import net.fabledrealms.fr.player.data.PlayerEcsData;
import net.fabledrealms.fr.player.data.component.ProfessionsComponent;
import net.fabledrealms.fr.player.data.model.CharacterProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Text-based administration UI for inspecting all stored character data.
 */
public final class AdminCharacterDataView {

    public List<String> render(PlayerEcsData data) {
        List<String> lines = new ArrayList<>();
        lines.add("=== FabledRealms Admin: Character Data ===");
        lines.add("Player: " + data.getLastKnownName() + " (" + data.getPlayerId() + ")");
        lines.add("ActiveCharacterId: " + data.getActiveCharacterId());
        lines.add("CreatedAt: " + data.getCreatedAt() + " | UpdatedAt: " + data.getUpdatedAt());
        lines.add("CharacterCount: " + data.getCharacters().size());

        int index = 1;
        for (Map.Entry<java.util.UUID, CharacterProfile> entry : data.getCharacters().entrySet()) {
            CharacterProfile character = entry.getValue();
            lines.add("--- Character #" + index + " ---");
            lines.add("Id: " + character.getCharacterId());
            lines.add("Name: " + character.getCharacterName());
            lines.add("CreatedAt: " + character.getCreatedAt() + " | UpdatedAt: " + character.getUpdatedAt());

            lines.add("Stats: level=" + character.getStats().getLevel()
                    + ", xp=" + character.getStats().getExperience()
                    + ", kills=" + character.getStats().getKills()
                    + ", deaths=" + character.getStats().getDeaths());

            lines.add("Economy: balance=" + character.getEconomy().getBalance());

            lines.add("Social: friends=" + character.getSocial().getFriends().size()
                    + ", ignored=" + character.getSocial().getIgnored().size());

            ProfessionsComponent professions = character.getProfessions();
            lines.add("Professions: "
                    + skill("mining", professions.getMining()) + ", "
                    + skill("farming", professions.getFarming()) + ", "
                    + skill("fishing", professions.getFishing()) + ", "
                    + skill("woodcutting", professions.getWoodcutting()) + ", "
                    + skill("skinning", professions.getSkinning()) + ", "
                    + skill("herblore", professions.getHerblore()) + ", "
                    + skill("cooking", professions.getCooking()) + ", "
                    + skill("smithing", professions.getSmithing()) + ", "
                    + skill("tailoring", professions.getTailoring()) + ", "
                    + skill("alchemy", professions.getAlchemy()) + ", "
                    + skill("enchanting", professions.getEnchanting()) + ", "
                    + skill("woodworking", professions.getWoodworking()));

            index++;
        }

        return lines;
    }

    private String skill(String name, ProfessionsComponent.SkillValue skill) {
        return name + "(lvl=" + skill.getLevel() + ",xp=" + skill.getExperience() + ")";
    }
}
