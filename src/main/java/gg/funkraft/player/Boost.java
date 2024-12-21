package gg.funkraft.player;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import gg.funkraft.ShootCraft;
import gg.funkraft.config.Config;

// Could make an interface for that + Gun.java but meh 
public class Boost {
    private ShootCraft instance;
    private ShootingPlayer player;
    public boolean canBoost;

    public Boost(ShootingPlayer player) {
        this.instance = ShootCraft.getInstance();
        this.player = player;
        this.canBoost = true;
    }

    public void removeAndRegainBoostHunger() {
        removeBoostHunger();
        regainBoostHunger();
    }

    private void removeBoostHunger() {
        player.getBukkitPlayer().setFoodLevel(20 - Config.boost.getHungerLossOnBoost());
        canBoost = false;
    }

    // Have to have those declated outside of regainBoostHunger
    int ranTicks;
    int hungerRegainAdd;
    private void regainBoostHunger() {
        int ticksPerHunger = Config.boost.getTicksPerHungerRegain();
        int hungerLossOnBoost = Config.boost.getHungerLossOnBoost();
        ranTicks = 1;
        hungerRegainAdd = 0;
        new BukkitRunnable() {
            @Override
            public void run() {
                ranTicks++;
                if (ranTicks % ticksPerHunger == 0) {
                    hungerRegainAdd++;
                    player.getBukkitPlayer().setFoodLevel(20 - hungerLossOnBoost + hungerRegainAdd);
                }
                if (hungerRegainAdd == hungerLossOnBoost) {
                    canBoost = true;
                    this.cancel();
                }
            }
        }.runTaskTimer(instance, 1, 1);
    }
    
    int ranBoostTicks;
    private void giveBoostEffectThenRevert() {
        giveSpeed(true);
        ranBoostTicks = 0;
        new BukkitRunnable() {
            @Override
            public void run() {
                ranBoostTicks++;
                if (ranBoostTicks == Config.boost.getBoostTime()) {
                    giveSpeed(false);
                    this.cancel();
                }
            }
        }.runTaskTimer(instance, 0, 1);
    }
 
    private void giveSpeed(boolean boost) {
        player.getBukkitPlayer().removePotionEffect(PotionEffectType.SPEED);
        player.getBukkitPlayer().addPotionEffect(new PotionEffect(
            PotionEffectType.SPEED, 9999999, boost ? 9 : 1, false, false)
        );
    }

    public boolean canBoost() {
        return canBoost;
    }

    public void boost() {
        if(player.killStreak <= 5) {

            giveBoostEffectThenRevert();
            removeAndRegainBoostHunger();

        } else if(player.killStreak <= 10) {

            player.getBukkitPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1, 255, false,  false));
            Bukkit.getWorld(player.getBukkitPlayer().getWorld().getUID()).spawnParticle(Particle.EXPLOSION_NORMAL, player.getBukkitPlayer().getLocation(), 5);
            Bukkit.getWorld(player.getBukkitPlayer().getWorld().getUID()).playSound(player.getBukkitPlayer().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f);

            for(Entity e : player.getBukkitPlayer().getNearbyEntities(5, 5, 5)) {
                if(e instanceof Player) {
                    ShootingPlayer target = new ShootingPlayer((Player)e);
                    target.hit(player.getBukkitPlayer().getName());
                    player.addKill();
                }
            }
            removeAndRegainBoostHunger();

        } else if(player.killStreak <= 15) {
            for(Player p : player.getBukkitPlayer().getWorld().getPlayers()) {
                if(p.equals(player.getBukkitPlayer())) return;
                p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 5, false,  false));
            }
        } else if(player.killStreak <= 20) {
            for(Entity e : player.getBukkitPlayer().getNearbyEntities(10, 10, 10)) {
                if(e.equals(player.getBukkitPlayer()) || !(e instanceof Player)) return;
                Player p = (Player)e;
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 3, false,  false));
            }
        }
    }
}
