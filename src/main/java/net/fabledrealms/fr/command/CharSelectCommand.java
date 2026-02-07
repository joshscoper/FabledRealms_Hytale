package net.fabledrealms.fr.command;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import net.fabledrealms.fr.player.FabledPlayerManager;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public final class CharSelectCommand extends AbstractCommand {

    private final FabledPlayerManager playerManager;

    public CharSelectCommand(FabledPlayerManager playerManager) {
        super("charselect", "Select a character by index");
        this.playerManager = playerManager;
        setAllowsExtraArguments(true);
    }

    @Override
    protected CompletableFuture<Void> execute(@Nonnull CommandContext ctx) {
        if (!ctx.isPlayer()) {
            ctx.sendMessage(Message.raw("Player-only command."));
            return CompletableFuture.completedFuture(null);
        }

        String[] parts = ctx.getInputString().trim().split("\\s+");
        if (parts.length < 2) {
            ctx.sendMessage(Message.raw("Usage: /charselect <index>"));
            return CompletableFuture.completedFuture(null);
        }

        int index;
        try {
            index = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            ctx.sendMessage(Message.raw("index must be a number."));
            return CompletableFuture.completedFuture(null);
        }

        Player player = ctx.senderAs(Player.class);
        try {
            var selected = playerManager.selectCharacterByIndex(player.getPlayerRef().getUuid(), index);
            ctx.sendMessage(Message.raw("Selected character: " + selected.name()));
        } catch (Exception e) {
            ctx.sendMessage(Message.raw("Selection failed: " + e.getMessage()));
        }

        return CompletableFuture.completedFuture(null);
    }
}
