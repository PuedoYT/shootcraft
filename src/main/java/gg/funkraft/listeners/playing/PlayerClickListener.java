package gg.funkraft.listeners.playing;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import gg.funkraft.PlayerManager;
import gg.funkraft.ShootCraft;
import gg.funkraft.player.Boost;
import gg.funkraft.player.Gun;
import gg.funkraft.player.ShootingPlayer;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerClickListener implements Listener {
    private PlayerManager playerMgr = ShootCraft.getInstance().getPlayerMgr();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ShootingPlayer p = playerMgr.getShootingPlayer(event.getPlayer());
        Action a = event.getAction();

        if (a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK)
            interactLeftClick(p);
        else if(event.getPlayer().isSneaking() && a == Action.RIGHT_CLICK_AIR || event.getPlayer().isSneaking() && a == Action.RIGHT_CLICK_BLOCK) {
            for(int i = 0; i < 3; i++) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Gun gun = p.getGun();
                        gun.fire();
                    }
                }.runTaskLater(ShootCraft.getInstance(), 20L);
            }
        }
        else if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK)
            interactRightClick(p);
    }

    public void interactRightClick(ShootingPlayer p) {
        Gun gun = p.getGun();
        // Logg
        if (gun.canFire())
            gun.fire();
    }

    public void interactLeftClick(ShootingPlayer p) {
        Boost boost = p.getBoost();
        if (boost.canBoost())
            boost.boost();
    }
}
