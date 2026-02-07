package net.fabledrealms.fr.ui;

import com.hypixel.hytale.server.core.entity.entities.Player;
import net.fabledrealms.fr.player.model.CharacterSummary;

import java.util.List;

public interface CharacterUiService {

    boolean openCharacterUi(Player player, String playerName, List<CharacterSummary> roster);
}
