package gg.funkraft.listeners.playing;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import gg.funkraft.PlayerManager;
import gg.funkraft.ShootCraft;
import gg.funkraft.mongodb.SpigotDatabaseManager;
import gg.funkraft.player.Boost;
import gg.funkraft.player.ShootingPlayer;
import gg.funkraft.reflections.packets.HandleParticleSend;
import gg.funkraft.reflections.packets.ParticleEnum;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.io.UnsupportedEncodingException;
import java.util.logging.Filter;

public class PlayerMoveTrailEvent implements Listener {

    private PlayerManager playerMgr = ShootCraft.getInstance().getPlayerMgr();

    @EventHandler
    public void onMove(PlayerMoveEvent e) throws UnsupportedEncodingException {
        Boost boost = playerMgr.getShootingPlayer(e.getPlayer()).getBoost();
        MongoCollection<Document> doc = SpigotDatabaseManager.getDatabase.getCollection("test");
        if(!boost.canBoost()) {
            Player p = e.getPlayer();
            Location loc = p.getLocation();
            new HandleParticleSend(ParticleEnum.getFromString(doc.find(Filters.eq("uuid", e.getPlayer().getUniqueId().toString())).first().getString("particle"))
                    ,loc.getX(), loc.getY() + 1, loc.getZ(), 0, 0, 0, 0, 5, null)
                    .sendPacketAllPlayers();
        }
    }
}
