package net.fabledrealms.fr.player.model;

import java.util.UUID;

public record CharacterSummary(
        UUID characterId,
        String name,
        int level,
        boolean active
) {
}
