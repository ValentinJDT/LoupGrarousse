package fr.mrcoq;

import fr.mrcoq.command.WWCommand;
import fr.mrcoq.command.WWTabCompleter;
import fr.mrcoq.game.Game;
import org.bukkit.plugin.java.JavaPlugin;

public final class LoupGarousse extends JavaPlugin {

    private static LoupGarousse plugin;
    private static final Game game = new Game();

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        registers();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registers() {
        registerCommands();
    }

    private void registerCommands() {
        getCommand("lg").setExecutor(new WWCommand());
        getCommand("lg").setTabCompleter(new WWTabCompleter());
    }

    public static Game getGame() {
        return game;
    }

    public static LoupGarousse getInstance() {
        return plugin;
    }
}
