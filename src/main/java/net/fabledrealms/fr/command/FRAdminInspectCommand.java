package net.fabledrealms.fr.command;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import net.fabledrealms.fr.FabledRealmsPlugin;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public final class FRAdminInspectCommand extends AbstractCommand {

    private final FabledRealmsPlugin plugin;

    public FRAdminInspectCommand(FabledRealmsPlugin plugin) {
        super("fradmin", "Game master tools for FabledRealms");
        this.plugin = plugin;
        setAllowsExtraArguments(true);
    }

    @Override
    protected CompletableFuture<Void> execute(@Nonnull CommandContext ctx) {
        String[] parts = ctx.getInputString().trim().split("\\s+");

        if (parts.length < 3 || !"inspect".equalsIgnoreCase(parts[1])) {
            ctx.sendMessage(Message.raw("Usage: /fradmin inspect <playerName|uuid>"));
            return CompletableFuture.completedFuture(null);
        }

        String target = parts[2];
        plugin.runAdminInspectCommand(target, line -> ctx.sendMessage(Message.raw(line)));
        return CompletableFuture.completedFuture(null);
    }
}
