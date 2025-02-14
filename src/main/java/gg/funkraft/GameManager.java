package gg.funkraft;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import gg.funkraft.config.Config;
import gg.funkraft.enums.GameState;
import gg.funkraft.player.ShootingPlayer;

public class GameManager {
    private ShootCraft shootCraft;
    private PlayerManager playerMgr;

    public GameManager() {
        shootCraft = ShootCraft.getInstance();
        playerMgr = shootCraft.getPlayerMgr();
        GameState.setGameState(GameState.IDLING);
    }

    public void broadcastGame(String message) {
        for (ShootingPlayer p : playerMgr.getOnlinePlayers())
            p.getBukkitPlayer().sendMessage(message);
    }
    public void broadcastGamePrefix(String message) {
        for (ShootingPlayer p : playerMgr.getOnlinePlayers())
            p.getBukkitPlayer().sendMessage(Config.game.getBroadcastPrefix() + message);
    }

    public void startGame() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            playerMgr.addPlayer(player);
        }


        GameState.setGameState(GameState.PLAYING);
    }

    public void stopGame() {
        GameState.setGameState(GameState.PLAYING);
    }
}
