package net.fabledrealms.fr.command;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import net.fabledrealms.fr.player.FabledPlayerManager;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public final class CharCreateCommand extends AbstractCommand {

    private final FabledPlayerManager playerManager;

    public CharCreateCommand(FabledPlayerManager playerManager) {
        super("charcreate", "Create a new character");
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
            ctx.sendMessage(Message.raw("Usage: /charcreate <name>"));
            return CompletableFuture.completedFuture(null);
        }

        Player player = ctx.senderAs(Player.class);
        try {
            var created = playerManager.createCharacter(player.getPlayerRef().getUuid(), parts[1]);
            ctx.sendMessage(Message.raw("Created character: " + created.name()));
        } catch (Exception e) {
            ctx.sendMessage(Message.raw("Create failed: " + e.getMessage()));
        }

        return CompletableFuture.completedFuture(null);
    }
}
