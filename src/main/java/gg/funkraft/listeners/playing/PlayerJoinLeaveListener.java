package gg.funkraft.listeners.playing;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import gg.funkraft.PlayerManager;
import gg.funkraft.ShootCraft;
import gg.funkraft.player.ShootingPlayer;

public class PlayerJoinLeaveListener implements Listener {
    private PlayerManager playerMgr = ShootCraft.getInstance().getPlayerMgr();
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        ShootingPlayer shootingP = playerMgr.getOfflineShootingPlayer(p);
        if (shootingP == null) {
            playerMgr.addPlayer(p);
        } else {
            playerMgr.rejoinPlayer(p);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        playerMgr.logoutPlayer(event.getPlayer());
    }
}
