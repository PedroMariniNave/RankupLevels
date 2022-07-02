package com.zpedroo.rankuplevels.listeners;

import com.zpedroo.rankuplevels.api.events.PlayerGainXpEvent;
import com.zpedroo.rankuplevels.api.events.PlayerUpgradeLevelEvent;
import com.zpedroo.rankuplevels.managers.DataManager;
import com.zpedroo.rankuplevels.objects.PlayerData;
import com.zpedroo.rankuplevels.objects.SoundProperties;
import com.zpedroo.rankuplevels.utils.config.Sounds;
import com.zpedroo.rankuplevels.utils.config.Titles;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerGeneralListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        PlayerData data = DataManager.getInstance().getPlayerData(event.getPlayer());
        data.updatePermissions();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        PlayerData data = DataManager.getInstance().getPlayerData(event.getPlayer());
        data.clearPermissions();

        DataManager.getInstance().savePlayerData(data);
    }

    @EventHandler
    public void onUpgradeLevel(PlayerUpgradeLevelEvent event) {
        Player player = event.getPlayer();
        player.sendTitle(Titles.UPGRADE[0], Titles.UPGRADE[1]);

        SoundProperties soundProperties = Sounds.UPGRADE;
        if (soundProperties != null && soundProperties.isEnabled()) {
            player.playSound(player.getLocation(), soundProperties.getSound(), soundProperties.getVolume(), soundProperties.getPitch());
        }
    }

    @EventHandler
    public void onGainXp(PlayerGainXpEvent event) {
        Player player = event.getPlayer();
        double xpAmount = event.getXpAmount();

        // check clothes
    }
}