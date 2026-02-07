package net.fabledrealms.fr.ui;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import net.fabledrealms.fr.player.model.CharacterSummary;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Uses the asset-pack UI id first and sends payload through common 2-arg UI API shapes.
 */
public final class HytaleCharacterUiService implements CharacterUiService {

    private static final String CHARACTER_MENU_UI_ID = "Common/UI/Custom/FabledRealms/CharacterMenu";

    private final CharacterUiPayloadBuilder payloadBuilder = new CharacterUiPayloadBuilder();

    @Override
    public boolean openCharacterUi(Player player, String playerName, List<CharacterSummary> roster) {
        String payload = payloadBuilder.build(playerName, roster);

        // Preferred: explicit UI id + payload.
        if (tryInvoke(player, "openUi", new Class<?>[]{String.class, String.class}, CHARACTER_MENU_UI_ID, payload)) return true;
        if (tryInvoke(player, "showUi", new Class<?>[]{String.class, String.class}, CHARACTER_MENU_UI_ID, payload)) return true;
        if (tryInvoke(player, "openUI", new Class<?>[]{String.class, String.class}, CHARACTER_MENU_UI_ID, payload)) return true;

        // Fallback: open UI then send payload event.
        boolean opened = tryInvoke(player, "openUi", new Class<?>[]{String.class}, CHARACTER_MENU_UI_ID)
                || tryInvoke(player, "showUi", new Class<?>[]{String.class}, CHARACTER_MENU_UI_ID)
                || tryInvoke(player, "openUI", new Class<?>[]{String.class}, CHARACTER_MENU_UI_ID);

        if (opened) {
            tryInvoke(player, "sendUiEvent", new Class<?>[]{String.class, String.class}, CHARACTER_MENU_UI_ID, payload);
            return true;
        }

        return tryInvoke(player, "sendMessage", new Class<?>[]{Message.class},
                Message.raw("Character UI unavailable in this server build. Use /chars for text menu."));
    }

    private boolean tryInvoke(Object target, String methodName, Class<?>[] signature, Object... args) {
        try {
            Method method = target.getClass().getMethod(methodName, signature);
            method.invoke(target, args);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }
}
