package com.zpedroo.rankuplevels.listeners;

import com.zpedroo.rankuplevels.api.events.PlayerGainXpEvent;
import com.zpedroo.rankuplevels.api.events.PlayerUpgradeLevelEvent;
import com.zpedroo.rankuplevels.managers.DataManager;
import com.zpedroo.rankuplevels.objects.Clothes;
import com.zpedroo.rankuplevels.objects.LevelInfo;
import com.zpedroo.rankuplevels.objects.PlayerData;
import com.zpedroo.rankuplevels.objects.SoundProperties;
import com.zpedroo.rankuplevels.utils.actionbar.ActionBarAPI;
import com.zpedroo.rankuplevels.utils.config.Sounds;
import com.zpedroo.rankuplevels.utils.config.Titles;
import de.tr7zw.nbtapi.NBTItem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import static com.zpedroo.rankuplevels.utils.config.Settings.*;

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
        int oldLevel = event.getOldLevel();
        int newLevel = event.getNewLevel();
        LevelInfo oldLevelInfo = event.getOldLevelInfo();
        LevelInfo newLevelInfo = event.getNewLevelInfo();

        String[] placeholders = new String[]{
                "{player}", "{old_level}", "{new_level}",
                "{old_level_tag}", "{new_level_tag}"
        };
        String[] replacers = new String[]{
                player.getName(), String.valueOf(oldLevel), String.valueOf(newLevel),
                StringUtils.replace(oldLevelInfo.getTag(), "{level}", String.valueOf(oldLevel)),
                StringUtils.replace(newLevelInfo.getTag(), "{level}", String.valueOf(newLevel)),
        };

        if (newLevelInfo.getActionBar() != null) {
            String text = newLevelInfo.getActionBar();
            ActionBarAPI.sendActionBarToAllPlayers(StringUtils.replaceEach(text, placeholders, replacers));
        }

        for (String message : newLevelInfo.getMessages()) {
            Bukkit.broadcastMessage(StringUtils.replaceEach(message, placeholders, replacers));
        }

        for (String command : newLevelInfo.getUpgradeCommands()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), StringUtils.replaceEach(command, placeholders, replacers));
        }

        if (THUNDER_ENABLED) {
            player.getWorld().spigot().strikeLightningEffect(player.getLocation(), SILENT_THUNDER_ENABLED);
            if (DOUBLE_THUNDER_ENABLED) {
                player.getWorld().spigot().strikeLightningEffect(player.getLocation(), SILENT_THUNDER_ENABLED);
            }
        }

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
        double bonus = 1;

        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (item == null || item.getType().equals(Material.AIR)) continue;

            NBTItem nbt = new NBTItem(item);
            if (!nbt.hasKey("Clothes")) continue;

            Clothes clothes = nbt.getObject("Clothes", Clothes.class);

        }

        event.setXpAmount(xpAmount * bonus);
    }
}