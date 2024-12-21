package gg.funkraft.commands;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import gg.funkraft.ShootCraft;
import gg.funkraft.enums.GameState;
import gg.funkraft.mongodb.SpigotDatabaseManager;
import gg.funkraft.reflections.packets.ParticleEnum;
import gg.funkraft.utils.logger.Logger;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class DevAbilitiesCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {

        if(arg0 instanceof Player){
            Player player = (Player) arg0;
            MongoCollection<Document> doc = SpigotDatabaseManager.getDatabase.getCollection("test");

            doc.updateOne(Filters.eq("uuid", player.getUniqueId().toString()), Filters.eq("particle", arg3[0]));
            player.sendMessage("§aParticule sélectionée mise à jour!");
        }

        return false;
    }


    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        ArrayList<String> particles = new ArrayList<>();
        for(ParticleEnum particle : ParticleEnum.values()) {
            particles.add(particle.toString());
        }
        return particles;
    }
}
