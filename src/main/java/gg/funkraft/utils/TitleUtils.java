package gg.funkraft.utils;

import org.bukkit.entity.Player;

import gg.funkraft.reflections.packets.HandleActionBarTitle;

public class TitleUtils {
    public static void sendActionBar(Player p, String text) {
        new HandleActionBarTitle(text).sendPacketPlayer(p);
    }
}
