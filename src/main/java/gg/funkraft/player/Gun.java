package gg.funkraft.player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import gg.funkraft.mongodb.SpigotDatabaseManager;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import lombok.Getter;
import gg.funkraft.GameManager;
import gg.funkraft.PlayerManager;
import gg.funkraft.ShootCraft;
import gg.funkraft.config.Config;
import gg.funkraft.reflections.packets.HandleParticleSend;
import gg.funkraft.reflections.packets.ParticleEnum;
import gg.funkraft.utils.ItemBuilder;

public class Gun {
    @Getter
    private static ItemStack itemEnabled = new ItemBuilder(Material.GOLD_HOE).itemName("§2[Enabled]§r ShootGun").build();
    @Getter
    private static ItemStack itemDisabled = new ItemBuilder(Material.STICK).itemName("§7[Reloading]§r ShootGun").build();

    @Getter
    private int delay;

    private World world;
    private ShootingPlayer player;
    private ShootCraft instance;
    private GameManager gameMgr;
    private PlayerManager playerMgr;

    private MongoCollection<Document> doc = SpigotDatabaseManager.getDatabase.getCollection("test");

    public Gun(ShootingPlayer player) {
        this.world = Config.map.getWorld();
        this.instance = ShootCraft.getInstance();
        this.playerMgr = instance.getPlayerMgr();
        this.gameMgr = instance.getGameMgr();
        this.player = player;
    }

    public void onRespawn() {
        setDelay(Config.spawn.getRespawnDuration());
    }

    public void setXpPercentage(int current, int max) {
        float xp = ((float) delay / max);
        player.getBukkitPlayer().setExp(1 - xp);
    }

    public void setDelay(int newDelay) {
        player.getBukkitPlayer().getInventory().setItem(4, Gun.getItemDisabled());
        this.delay = newDelay;
        new BukkitRunnable() {
            @Override
            public void run() {
                delay--;
                setXpPercentage(delay, newDelay);
                if (delay == 0) {
                    setXpPercentage(1, 1);
                    player.getBukkitPlayer().getInventory().setItem(4, Gun.getItemEnabled());
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(instance, 0, 1);
    }

    public boolean canFire() {
        return delay <= 0; // Could be == 0 but just in case
    }

    public void fire() {
        setDelay(Config.gun.getGunDelayDuration());

        Set<Player> hitPlayers = new HashSet<>();
        String name = player.getBukkitPlayer().getDisplayName();

        // values straight from the player
        Player p = player.getBukkitPlayer();
        Location loc = p.getLocation();
        Vector direction = loc.getDirection().divide(new Vector(2, 2, 2));
        double x = loc.getX();
        double y = loc.getY() + 1.5; //getY = base of feet, +1.5 = head
        double z = loc.getZ();

        // TODO: if possible, redo this detection in a better way
        // Basically before the particles loop, calculate
        // the players that have been hit.
        // https://www.spigotmc.org/threads/hitboxes-and-ray-tracing.174358/page-3
        // https://bukkit.org/threads/using-rays-to-quickly-and-accurately-detect-hitbox-collisions.441877/
        // 
        // EDIT: for now in standby.
        // Only minimal gains from this so not doing it yet. For retry later:
        // (scroll, initial code is bad) https://www.spigotmc.org/threads/particle-ray-with-block-hitbox.327728/
        //
        // https://www.spigotmc.org/threads/hitboxes-and-ray-tracing.174358/
        // -> https://pastebin.com/eHQawAme
        // -> https://pastebin.com/PV63ZkQy
        //
        // https://gist.github.com/aadnk/7123926


        for (int i = 0; i < 120; i++) {
            x += direction.getX();
            z += direction.getZ();
            y += direction.getY();

            loc = new Location(world, x, y, z);
            if (loc.getBlock().getType() != Material.AIR) {
                // If hits wall (may be bypassable if through corners)
                break;
            }

            // Could use a better system with loc.distance(otherloc) <= x
            // but that's already setup and working relatively well
            Collection<Entity> nearbyEntities = world.getNearbyEntities(loc, .2, .2, .2); // VALUES TO TWEAK


            for (Entity e : nearbyEntities) {
                if (!(e instanceof Player))
                    continue;

                Player hitPlayer = (Player) e;
                if (hitPlayers.contains(hitPlayer) || hitPlayer == p)
                    continue;

                if (playerMgr.getShootingPlayer(hitPlayer).hit(name)) {
                    switch (doc.find(Filters.eq("uuid", player.getBukkitPlayer().getUniqueId().toString())).first().getString("killeffect").toLowerCase()) {
                        case "boom":
                            Location location = player.getBukkitPlayer().getLocation();
                            //new HandleParticleSend(ParticleEnum.EXPLOSION_NORMAL, loc.getX(), loc.getY(), loc.getZ(), 0, 0, 0, 1, 5, null);
                            hitPlayer.getWorld().playSound(hitPlayer.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f);
                            break;
                    }
                    hitPlayers.add(hitPlayer);
                }

                // Only send 1 in 2 particle, so that checks always run
                // but particles don't appear clogged.
                // if (i % 2 == 0)
                new HandleParticleSend(ParticleEnum.FIREWORKS_SPARK, x, y, z, 0, 0, 0, 0, 1, null)
                        .sendPacketAllPlayers();
            }
            int hitCount = hitPlayers.size();
            if (hitCount > 1) {
                gameMgr.broadcastGamePrefix(p.getDisplayName() + " a tué " + hitCount + " joueurs en un seul tir !");
                if (Config.lives.isLiveOnDoubleKill()) {
                    player.addLife();
                }
            }
            for (int j = 0; j < hitCount; j++) {
                player.addKill();
            }
        }
    }
}
