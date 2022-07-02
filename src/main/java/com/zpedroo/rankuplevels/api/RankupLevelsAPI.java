package com.zpedroo.rankuplevels.api;

import com.zpedroo.rankuplevels.managers.DataManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RankupLevelsAPI {

    public static int getLevel(@NotNull Player player) {
        return DataManager.getInstance().getPlayerData(player).getLevel();
    }

    public static void addExp(@NotNull Player player, double amount) {
        DataManager.getInstance().getPlayerData(player).addExp(amount);
    }

    public static void setExp(@NotNull Player player, double amount) {
        DataManager.getInstance().getPlayerData(player).setExpAmount(amount);
    }
}