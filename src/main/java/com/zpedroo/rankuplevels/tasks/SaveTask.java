package com.zpedroo.rankuplevels.tasks;

import com.zpedroo.rankuplevels.managers.DataManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import static com.zpedroo.rankuplevels.utils.config.Settings.SAVE_INTERVAL;

public class SaveTask extends BukkitRunnable {

    public SaveTask(Plugin plugin) {
        this.runTaskTimerAsynchronously(plugin, SAVE_INTERVAL * 20L, SAVE_INTERVAL * 20L);
    }

    @Override
    public void run() {
        DataManager.getInstance().saveAllPlayersData();
        DataManager.getInstance().updateTopRanks();
    }
}