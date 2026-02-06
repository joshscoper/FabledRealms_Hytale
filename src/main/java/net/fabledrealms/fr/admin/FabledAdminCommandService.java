package net.fabledrealms.fr.admin;

import net.fabledrealms.fr.player.FabledPlayerManager;
import net.fabledrealms.fr.player.data.PlayerEcsData;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Command service for GM administration commands.
 *
 * <p>Use {@code /fradmin inspect <playerName|uuid>} as the intended command shape.
 * Hook this service into Hytale's command API in your listener/registration layer.</p>
 */
public final class FabledAdminCommandService {

    private final FabledPlayerManager playerManager;
    private final AdminCharacterDataView dataView;

    public FabledAdminCommandService(FabledPlayerManager playerManager) {
        this.playerManager = playerManager;
        this.dataView = new AdminCharacterDataView();
    }

    public void inspectPlayerCharacters(String target, Consumer<String> reply) {
        Optional<PlayerEcsData> data = parseUuid(target)
                .flatMap(playerManager::getPlayerData)
                .or(() -> playerManager.getPlayerDataByName(target));

        if (data.isEmpty()) {
            reply.accept("No player data found for target: " + target);
            return;
        }

        List<String> lines = dataView.render(data.get());
        lines.forEach(reply);
    }

    private Optional<UUID> parseUuid(String value) {
        try {
            return Optional.of(UUID.fromString(value));
        } catch (IllegalArgumentException ignored) {
            return Optional.empty();
        }
    }
}
