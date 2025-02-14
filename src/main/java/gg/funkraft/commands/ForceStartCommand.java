package gg.funkraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import gg.funkraft.ShootCraft;
import gg.funkraft.enums.GameState;
import gg.funkraft.utils.logger.Logger;

public class ForceStartCommand implements CommandExecutor {
    ShootCraft instance = ShootCraft.getInstance();

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        Logger.logBC("Force starting game");

        if (GameState.getCurrentState() == GameState.PLAYING) {
            instance.getGameMgr().stopGame();
        } else {
            instance.getGameMgr().startGame();
        }

        return true;
    }
}
