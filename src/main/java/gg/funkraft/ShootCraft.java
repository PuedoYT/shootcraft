package gg.funkraft;

import gg.funkraft.commands.DevAbilitiesCommand;
import gg.funkraft.commands.DevStats;
import gg.funkraft.enums.GameState;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import gg.funkraft.commands.ForceStartCommand;
import gg.funkraft.config.Config;

public class ShootCraft extends JavaPlugin {
    @Getter
    private static ShootCraft instance;
    @Getter
    private PluginManager pluginMgr;
    @Getter
    private GameManager gameMgr;
    @Getter
    private PlayerManager playerMgr;


    @Override
    public void onDisable() {
        GameState.setGameState(GameState.IDLING);
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        Config.init(getConfig());

        pluginMgr = getServer().getPluginManager();

        playerMgr = new PlayerManager();
        gameMgr = new GameManager();

        getCommand("force_start").setExecutor(new ForceStartCommand());
        getCommand("stats").setExecutor(new DevStats());
        getCommand("stats").setTabCompleter(new DevStats());
        getCommand("particle").setExecutor(new DevAbilitiesCommand());
        getCommand("particle").setTabCompleter(new DevAbilitiesCommand());

        gameMgr.broadcastGamePrefix("Plugin enabled successfully.");
    }

    // TODO left:
    // - maxGameDuration
    // - maxPlayerCount
    // - scoreboard
    // - game ending
    // - actually use TitleUtils
}