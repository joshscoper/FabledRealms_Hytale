package net.fabledrealms.fr.ui;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import java.lang.reflect.Method;

public final class JoinCharacterMenuPrompt {

    public boolean trySendPrompt(PlayerRef playerRef) {
        String[] lines = {
                "=== FabledRealms ===",
                "Use /chars to open character selection.",
                "Create: /charcreate <name>",
                "Select: /charselect <index>",
                "Delete: /chardelete <index>"
        };

        for (String line : lines) {
            if (!trySendMessage(playerRef, line)) {
                return false;
            }
        }

        return true;
    }

    private boolean trySendMessage(PlayerRef playerRef, String text) {
        try {
            Method sendMessage = playerRef.getClass().getMethod("sendMessage", Message.class);
            sendMessage.invoke(playerRef, Message.raw(text));
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }
}
