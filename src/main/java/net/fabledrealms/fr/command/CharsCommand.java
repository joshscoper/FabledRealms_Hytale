package net.fabledrealms.fr.command;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import net.fabledrealms.fr.player.FabledPlayerManager;
import net.fabledrealms.fr.ui.CharacterMenuRenderer;
import net.fabledrealms.fr.ui.CharacterUiService;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public final class CharsCommand extends AbstractCommand {

    private final FabledPlayerManager playerManager;
    private final CharacterMenuRenderer renderer;
    private final CharacterUiService uiService;

    public CharsCommand(FabledPlayerManager playerManager,
                        CharacterMenuRenderer renderer,
                        CharacterUiService uiService) {
        super("chars", "Open character selection UI");
        this.playerManager = playerManager;
        this.renderer = renderer;
        this.uiService = uiService;
    }

    @Override
    protected CompletableFuture<Void> execute(@Nonnull CommandContext ctx) {
        if (!ctx.isPlayer()) {
            ctx.sendMessage(Message.raw("Player-only command."));
            return CompletableFuture.completedFuture(null);
        }

        Player player = ctx.senderAs(Player.class);
        var roster = playerManager.getRoster(player.getPlayerRef().getUuid());
        String name = playerManager.getPlayerData(player.getPlayerRef().getUuid())
                .map(data -> data.getLastKnownName())
                .orElse(player.getDisplayName());

        boolean opened = uiService.openCharacterUi(player, name, roster);
        if (!opened) {
            renderer.renderToCommand(ctx, name, roster);
        }

        return CompletableFuture.completedFuture(null);
    }
}
