package gg.funkraft.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import gg.funkraft.ShootCraft;
import gg.funkraft.config.inner.BoostConfig;
import gg.funkraft.config.inner.GameConfig;
import gg.funkraft.config.inner.GunConfig;
import gg.funkraft.config.inner.LivesConfig;
import gg.funkraft.config.inner.MapConfig;
import gg.funkraft.config.inner.SpawnConfig;

public class Config {

    private static ShootCraft plugin = ShootCraft.getInstance();
    private static FileConfiguration fileConf;
    public static BoostConfig boost;
    public static GameConfig game;
    public static GunConfig gun;
    public static LivesConfig lives;
    public static MapConfig map;
    public static SpawnConfig spawn;

    public static void init(FileConfiguration conf) {
        Config.fileConf = conf;
        boost = new BoostConfig(getFileConfigBlock("boost"));
        game = new GameConfig(getFileConfigBlock("game"));
        gun = new GunConfig(getFileConfigBlock("gun"));
        lives = new LivesConfig(getFileConfigBlock("lives"));
        map = new MapConfig(getFileConfigBlock("map"));
        spawn = new SpawnConfig(getFileConfigBlock("spawn"));
    }

    public static void saveConfig() {
        fileConf.options().copyDefaults(true);
        plugin.saveConfig();
    }

    public static ConfigurationSection getFileConfigBlock(String str) {
        return fileConf.getConfigurationSection(str);
    }
}
