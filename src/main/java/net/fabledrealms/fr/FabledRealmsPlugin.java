package net.fabledrealms.fr;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import net.fabledrealms.fr.admin.FabledAdminCommandService;
import net.fabledrealms.fr.command.CharCreateCommand;
import net.fabledrealms.fr.command.CharDeleteCommand;
import net.fabledrealms.fr.command.CharSelectCommand;
import net.fabledrealms.fr.command.CharsCommand;
import net.fabledrealms.fr.command.FRAdminInspectCommand;
import net.fabledrealms.fr.lifecycle.PlayerLifecycleModule;
import net.fabledrealms.fr.player.FabledPlayerManager;
import net.fabledrealms.fr.session.SessionStore;
import net.fabledrealms.fr.ui.CharacterMenuRenderer;
import net.fabledrealms.fr.ui.CharacterUiService;
import net.fabledrealms.fr.ui.HytaleCharacterUiService;
import net.fabledrealms.fr.ui.JoinCharacterMenuPrompt;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public final class FabledRealmsPlugin extends JavaPlugin {

    private FabledPlayerManager playerManager;
    private FabledAdminCommandService adminCommandService;
    private PlayerLifecycleModule playerLifecycle;

    private SessionStore sessionStore;
    private CharacterMenuRenderer characterMenuRenderer;
    private CharacterUiService characterUiService;

    public FabledRealmsPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    public void start() {
        this.playerManager = new FabledPlayerManager(this);
        this.adminCommandService = new FabledAdminCommandService(playerManager);

        this.sessionStore = new SessionStore();
        this.characterMenuRenderer = new CharacterMenuRenderer();
        this.characterUiService = new HytaleCharacterUiService();

        this.playerLifecycle = new PlayerLifecycleModule(
                getLogger(),
                playerManager,
                sessionStore,
                new JoinCharacterMenuPrompt(),
                characterUiService
        );
        this.playerLifecycle.register(getEventRegistry());

        getCommandRegistry().registerCommand(new CharsCommand(playerManager, characterMenuRenderer, characterUiService));
        getCommandRegistry().registerCommand(new CharCreateCommand(playerManager));
        getCommandRegistry().registerCommand(new CharSelectCommand(playerManager));
        getCommandRegistry().registerCommand(new CharDeleteCommand(playerManager));
        getCommandRegistry().registerCommand(new FRAdminInspectCommand(this));

        getLogger().atInfo().log("FabledRealms started with MMORPG character foundation (create/select/delete + JSON persistence).\n"
                + "UI path: native Hytale UI hooks first, chat fallback second.");
    }

    @Override
    public void shutdown() {
        if (playerLifecycle != null) {
            playerLifecycle.close();
        }

        if (playerManager != null) {
            playerManager.saveAll();
        }

        getLogger().atInfo().log("FabledRealms shutdown complete. Player data flushed to JSON.");
    }

    public void runAdminInspectCommand(String target, Consumer<String> reply) {
        adminCommandService.inspectPlayerCharacters(target, reply);
    }

    public FabledPlayerManager getPlayerManager() {
        return playerManager;
    }

    public FabledAdminCommandService getAdminCommandService() {
        return adminCommandService;
    }

    public SessionStore getSessionStore() {
        return sessionStore;
    }
}
