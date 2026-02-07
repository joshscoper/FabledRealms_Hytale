package net.fabledrealms.fr.ui;

import net.fabledrealms.fr.player.model.CharacterSummary;

import java.util.List;

public final class CharacterUiPayloadBuilder {

    public String build(String playerName, List<CharacterSummary> roster) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"type\":\"fabledrealms.character.menu\",\"player\":\"")
                .append(escape(playerName))
                .append("\",\"characters\":[");

        for (int i = 0; i < roster.size(); i++) {
            CharacterSummary c = roster.get(i);
            if (i > 0) sb.append(',');
            sb.append("{\"index\":").append(i)
                    .append(",\"id\":\"").append(c.characterId()).append("\"")
                    .append(",\"name\":\"").append(escape(c.name())).append("\"")
                    .append(",\"level\":").append(c.level())
                    .append(",\"active\":").append(c.active())
                    .append('}');
        }

        sb.append("]}");
        return sb.toString();
    }

    private String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
