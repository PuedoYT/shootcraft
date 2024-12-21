package gg.funkraft.player;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;
import gg.funkraft.GameManager;
import gg.funkraft.ShootCraft;
import gg.funkraft.config.Config;

public class ShootingPlayer {
    private GameManager gameMgr;

    @Getter
    private Gun gun;
    @Getter
    private Boost boost;
    private RespawnManager respawnManager;

    private int currentLives;

    @Getter @Setter
    public int killStreak;
    @Getter @Setter
    private int totalKills;

    @Getter
    @Setter
    private boolean isProtected;

    @Getter
    private Player bukkitPlayer;
    @SuppressWarnings("unused")
    private boolean isOnline;

    public ShootingPlayer(Player bukkitPlayer) {
        this.gameMgr = ShootCraft.getInstance().getGameMgr();
        this.bukkitPlayer = bukkitPlayer;
        this.isOnline = true;
        this.gun = new Gun(this);
        this.boost = new Boost(this);
        this.respawnManager = new RespawnManager(this);
        initPlayer();
    }

    private void setMaxHealth() {
        bukkitPlayer.setMaxHealth(Config.lives.getMaxLives() * 2);
    }
 
    private void onInitOrRelog() {
        setMaxHealth();
        bukkitPlayer.getInventory().clear();
        bukkitPlayer.getInventory().setHeldItemSlot(4);
    }

    public void initPlayer() {
        this.currentLives = Config.lives.getStartingLives();
        this.killStreak = 0;
        onInitOrRelog();
        respawnManager.initialSpawn();
    }

    public void rejoin(Player player) {
        this.bukkitPlayer = player;
        this.isOnline = true;
        
        onInitOrRelog(); // May need to be delayed 1 tick

        kill();
    }

    public void leave() {
        this.bukkitPlayer = null;
        this.isOnline = false;
    }

    public void addKill() {
        this.totalKills++;
        this.killStreak++;
        if (Config.lives.getLiveOnKillStreak().contains(killStreak)) {
            String ending = (addLife()) ? " et a gagné une vie." : ". Il a déjà atteint le nombre maximum de vies.";
            gameMgr.broadcastGamePrefix(bukkitPlayer.getDisplayName() + " a fait un killstreak de " + killStreak + ending);
        }

        if (Config.game.isKfwEnabled() && Config.game.getKillsForWin() >= totalKills) {
            gameMgr.broadcastGamePrefix(Config.game.getBroadcastPrefix() + "§l§6PARTIE TERMINEE !");
            // TODO: end game here
        }
    }

    public boolean addLife() {
        if (this.currentLives >= Config.lives.getMaxLives())
            return false;
        this.currentLives++;
        bukkitPlayer.setHealth(currentLives * 2);
        
        bukkitPlayer.getInventory().setArmorContents(new ItemStack[]{null, null, new ItemStack(Material.DIAMOND_CHESTPLATE), null});

        return true;
    }


    /**
     * @param killer name of the killer
     * @return true if the hit was performed, false if the player was protected.
     */
    public boolean hit(String killer) {
        if (this.isProtected)
            return false;
            
        if (this.currentLives == 1) {
            kill();
            gameMgr.broadcastGame(Config.game.getBroadcastPrefix() + bukkitPlayer.getDisplayName() + " a été éliminé par " + killer);
            gameMgr.broadcastGame("Killstreak de " + killer + " : " + new ShootingPlayer(Bukkit.getPlayer(killer)).killStreak);
        } else {
            this.currentLives--;
            bukkitPlayer.setHealth(currentLives * 2);
            // Check after to see if on last life or not
            if (this.currentLives == 1) {
                bukkitPlayer.getInventory().setArmorContents(new ItemStack[]{null, null, null, null});
            }
        }
        return true;
    }

    private void kill() {
        gun.onRespawn();
        this.currentLives = Config.lives.getStartingLives();
        this.killStreak = 0;
        respawnManager.respawnAfterKill();
    }
}
