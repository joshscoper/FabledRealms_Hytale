package net.fabledrealms.fr.ui;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import net.fabledrealms.fr.player.model.CharacterSummary;

import java.util.List;

public final class CharacterMenuRenderer {

    public void renderToCommand(CommandContext ctx, String playerName, List<CharacterSummary> roster) {
        ctx.sendMessage(Message.raw("=== FabledRealms Character Menu ==="));
        ctx.sendMessage(Message.raw("Player: " + playerName));
        if (roster.isEmpty()) {
            ctx.sendMessage(Message.raw("No characters found."));
        } else {
            for (int i = 0; i < roster.size(); i++) {
                CharacterSummary summary = roster.get(i);
                String active = summary.active() ? " [ACTIVE]" : "";
                ctx.sendMessage(Message.raw("[" + i + "] " + summary.name() + " Lv." + summary.level() + active));
            }
        }

        ctx.sendMessage(Message.raw("Commands:"));
        ctx.sendMessage(Message.raw("/chars"));
        ctx.sendMessage(Message.raw("/charcreate <name>"));
        ctx.sendMessage(Message.raw("/charselect <index>"));
        ctx.sendMessage(Message.raw("/chardelete <index>"));
    }
}
