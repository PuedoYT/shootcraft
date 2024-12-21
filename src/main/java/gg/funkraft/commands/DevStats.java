package gg.funkraft.commands;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import gg.funkraft.mongodb.SpigotDatabaseManager;
import gg.funkraft.player.ShootingPlayer;
import gg.funkraft.reflections.packets.ParticleEnum;
import org.bson.Document;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DevStats implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {

        if(arg0 instanceof Player){
            ShootingPlayer player = new ShootingPlayer((Player) arg0);

            switch(arg3[0].toLowerCase()) {
                case "setkills":
                    player.setTotalKills(Integer.parseInt(arg3[1]));
                    player.getBukkitPlayer().sendMessage("Nouveau nb. total de kills: " + player.getTotalKills());
                    break;
                case "setkillstreak":
                    player.setKillStreak(Integer.parseInt(arg3[1]));
                    player.getBukkitPlayer().sendMessage("Nouveau killstreak: " + player.getKillStreak());
                    break;
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return Arrays.asList("setkills", "setkillstreak");
    }
}
