package net.fabledrealms.fr;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import net.fabledrealms.fr.admin.FabledAdminCommandService;
import net.fabledrealms.fr.player.FabledPlayerManager;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;

public final class FabledRealmsPlugin extends JavaPlugin {

    private FabledPlayerManager playerManager;
    private FabledAdminCommandService adminCommandService;

    public FabledRealmsPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    public void start() {
        this.playerManager = new FabledPlayerManager(this);
        this.adminCommandService = new FabledAdminCommandService(playerManager);

        registerEverythingInMainClass();
        getLogger().atInfo().log("FabledRealms started with JSON ECS player persistence + admin inspect wiring.");
    }

    @Override
    public void shutdown() {
        if (playerManager != null) {
            playerManager.saveAll();
        }

        getLogger().atInfo().log("FabledRealms shutdown complete. Player data flushed to JSON.");
    }

    /**
     * Centralized bootstrap for command + player lifecycle wiring.
     *
     * <p>Reflection keeps this resilient to Hytale API signature changes while
     * still registering everything from this main class as requested.</p>
     */
    private void registerEverythingInMainClass() {
        boolean commandRegistered = tryRegisterFrAdminCommand();
        boolean lifecycleRegistered = tryRegisterPlayerLifecycleHooks();

        if (!commandRegistered) {
            getLogger().atWarning().log("Could not auto-register /fradmin command with current server API. "
                    + "Hook runAdminInspectCommand(...) from your command binder.");
        }

        if (!lifecycleRegistered) {
            getLogger().atWarning().log("Could not auto-register player join/quit hooks with current server API. "
                    + "Hook onPlayerJoin/onPlayerQuit from your event binder.");
        }
    }

    private boolean tryRegisterFrAdminCommand() {
        Object server = invokeNoArg(this, "getServer");
        if (server == null) {
            return false;
        }

        // Common style: registerCommand(String, Consumer<String[]>)
        for (Method method : server.getClass().getMethods()) {
            if (!method.getName().toLowerCase().contains("register")) {
                continue;
            }

            Class<?>[] params = method.getParameterTypes();
            if (params.length != 2) {
                continue;
            }

            if (params[0] == String.class && params[1] == Consumer.class) {
                try {
                    method.invoke(server, "fradmin", (Consumer<String[]>) args -> {
                        if (args.length < 2 || !"inspect".equalsIgnoreCase(args[0])) {
                            return;
                        }
                        runAdminInspectCommand(args[1], line -> getLogger().atInfo().log("[FRAdmin] %s", line));
                    });
                    return true;
                } catch (Exception ignored) {
                    // continue trying other signatures
                }
            }
        }

        return false;
    }

    private boolean tryRegisterPlayerLifecycleHooks() {
        Object server = invokeNoArg(this, "getServer");
        if (server == null) {
            return false;
        }

        // Best-effort registration against likely method names/signatures.
        return invokeIfPresent(server, "onPlayerJoin", Player.class, (Consumer<Player>) this::onPlayerJoin)
                || invokeIfPresent(server, "registerPlayerJoinListener", Consumer.class, (Consumer<Player>) this::onPlayerJoin)
                || invokeIfPresent(server, "onPlayerQuit", Player.class, (Consumer<Player>) this::onPlayerQuit)
                || invokeIfPresent(server, "registerPlayerQuitListener", Consumer.class, (Consumer<Player>) this::onPlayerQuit);
    }

    private boolean invokeIfPresent(Object target, String methodName, Class<?> paramType, Object arg) {
        try {
            Method method = target.getClass().getMethod(methodName, paramType);
            method.invoke(target, arg);
            return true;
        } catch (NoSuchMethodException ignored) {
            return false;
        } catch (Exception e) {
            getLogger().atWarning().log("Failed invoking %s on %s with params %s", methodName,
                    target.getClass().getName(), Arrays.toString(new Object[]{arg}));
            return false;
        }
    }

    private Object invokeNoArg(Object target, String methodName) {
        try {
            Method method = target.getClass().getMethod(methodName);
            return method.invoke(target);
        } catch (Exception ignored) {
            return null;
        }
    }

    public void onPlayerJoin(Player player) {
        playerManager.loadPlayer(player);
    }

    public void onPlayerQuit(Player player) {
        UUID playerId = player.getUuid();
        playerManager.unloadPlayer(playerId);
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
}
