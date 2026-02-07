package net.fabledrealms.fr.ui;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import net.fabledrealms.fr.player.model.CharacterSummary;

import java.lang.reflect.Method;
import java.util.List;

public final class HytaleCharacterUiService implements CharacterUiService {

    private final CharacterUiPayloadBuilder payloadBuilder = new CharacterUiPayloadBuilder();

    @Override
    public boolean openCharacterUi(Player player, String playerName, List<CharacterSummary> roster) {
        String payload = payloadBuilder.build(playerName, roster);

        if (tryInvoke(player, "openUi", String.class, payload)) return true;
        if (tryInvoke(player, "openUI", String.class, payload)) return true;
        if (tryInvoke(player, "showUi", String.class, payload)) return true;
        if (tryInvoke(player, "showUI", String.class, payload)) return true;
        if (tryInvoke(player, "showModal", String.class, payload)) return true;
        if (tryInvoke(player, "showDialog", String.class, payload)) return true;
        if (tryInvoke(player, "sendUi", String.class, payload)) return true;

        return tryInvoke(player, "sendMessage", Message.class,
                Message.raw("Opening character UI is not available on this server build. Use /chars."));
    }

    private boolean tryInvoke(Object target, String methodName, Class<?> argType, Object arg) {
        try {
            Method method = target.getClass().getMethod(methodName, argType);
            method.invoke(target, arg);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }
}
