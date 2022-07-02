package com.zpedroo.rankuplevels.commands;

import com.zpedroo.rankuplevels.api.RankupLevelsAPI;
import com.zpedroo.rankuplevels.utils.config.Settings;
import com.zpedroo.rankuplevels.utils.formatter.NumberFormatter;
import com.zpedroo.rankuplevels.utils.menu.Menus;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigInteger;

public class RankupLevelsCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = sender instanceof Player ? (Player) sender : null;
        if (args.length > 0) {
            Player target = null;
            BigInteger amount = null;
            switch (args[0].toUpperCase()) {
                case "GIVE":
                    if (args.length < 3) break;
                    if (!sender.hasPermission(Settings.ADMIN_PERMISSION)) break;

                    target = Bukkit.getPlayer(args[1]);
                    if (target == null) break;

                    amount = NumberFormatter.getInstance().filter(args[2]);
                    if (amount.signum() <= 0) break;

                    RankupLevelsAPI.addExp(target, amount.doubleValue());
                    return true;
                case "SET":
                    if (args.length < 3) break;
                    if (!sender.hasPermission(Settings.ADMIN_PERMISSION)) break;

                    target = Bukkit.getPlayer(args[1]);
                    if (target == null) break;

                    amount = NumberFormatter.getInstance().filter(args[2]);
                    if (amount.signum() <= 0) break;

                    RankupLevelsAPI.setExp(target, amount.doubleValue());
                    return true;
            }
        }

        if (player != null) Menus.getInstance().openMainMenu(player);
        return false;
    }
}