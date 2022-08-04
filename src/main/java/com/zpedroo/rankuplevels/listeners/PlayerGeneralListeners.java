package com.zpedroo.rankuplevels.listeners;

import com.zpedroo.rankuplevels.api.events.PlayerGainXpEvent;
import com.zpedroo.rankuplevels.api.events.PlayerUpgradeLevelEvent;
import com.zpedroo.rankuplevels.managers.DataManager;
import com.zpedroo.rankuplevels.objects.clothes.Clothes;
import com.zpedroo.rankuplevels.objects.clothes.ClothesItem;
import com.zpedroo.rankuplevels.objects.general.LevelInfo;
import com.zpedroo.rankuplevels.objects.general.PlayerData;
import com.zpedroo.rankuplevels.objects.properties.SoundProperties;
import com.zpedroo.rankuplevels.utils.actionbar.ActionBarAPI;
import com.zpedroo.rankuplevels.utils.config.Messages;
import com.zpedroo.rankuplevels.utils.config.Sounds;
import com.zpedroo.rankuplevels.utils.config.Titles;
import com.zpedroo.rankuplevels.utils.formatter.NumberFormatter;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        handleNewLevelInfoMethods(newLevelInfo, placeholders, replacers);

        player.sendTitle(Titles.RANK_UP[0], Titles.RANK_UP[1]);
        SoundProperties soundProperties = Sounds.RANK_UP;
        if (soundProperties != null && soundProperties.isEnabled()) {
            player.playSound(player.getLocation(), soundProperties.getSound(), soundProperties.getVolume(), soundProperties.getPitch());
        }
    }

    @EventHandler
    public void onGainXp(PlayerGainXpEvent event) {
        Player player = event.getPlayer();
        double xpAmount = event.getXpAmount();
        double bonus = 1;

        ItemStack[] armorItems = player.getInventory().getArmorContents();
        ItemStack[] newArmorItems = new ItemStack[armorItems.length];

        Set<Integer> newLevelMessages = new HashSet<>(1);
        Set<Integer> newClothesMessages = new HashSet<>(1);
        for (int i = 0; i < armorItems.length; ++i) {
            ItemStack item = armorItems[i];
            if (item == null || item.getType().equals(Material.AIR)) continue;

            ClothesItem originalClothesItem = DataManager.getInstance().getClothesItem(item);
            if (originalClothesItem == null) {
                newArmorItems[i] = item;
                continue;
            }

            final int oldLevel = originalClothesItem.getLevel();
            originalClothesItem.addExperience(xpAmount);

            int newLevel = originalClothesItem.getLevel();
            Clothes newClothes = DataManager.getInstance().getClothesByLevel(newLevel);
            if (newClothes == null) continue;

            double totalXp = originalClothesItem.getExperience();
            ClothesItem newClothesItem = new ClothesItem(newClothes, newClothes.getSimilarItem(item), totalXp);
            double oldBonus = originalClothesItem.getTotalBonus();
            double newBonus = newClothesItem.getTotalBonus();
            double oldVisualBonus = originalClothesItem.getClothes().getBonusPerLevel();
            double newVisualBonus = newClothesItem.getClothes().getBonusPerLevel();
            String[] placeholders = new String[]{
                    "{old_level}", "{new_level}", "{old_bonus}", "{new_bonus}", "{old_visual_bonus}", "{new_visual_bonus}"
            };
            String[] replacers = new String[]{
                    NumberFormatter.getInstance().formatThousand(oldLevel), NumberFormatter.getInstance().formatThousand(newLevel),
                    NumberFormatter.getInstance().formatDecimal(oldBonus, CLOTHES_DIGITS), NumberFormatter.getInstance().formatDecimal(newBonus, CLOTHES_DIGITS),
                    NumberFormatter.getInstance().formatDecimal(oldVisualBonus, CLOTHES_DIGITS), NumberFormatter.getInstance().formatDecimal(newVisualBonus, CLOTHES_DIGITS)
            };

            boolean playEffects = newLevelMessages.add(newLevel);
            if (isNewLevel(oldLevel, newLevel) && playEffects) {
                sendMessages(player, Messages.NEW_LEVEL, placeholders, replacers);
                handleNewClothesSounds(player);
            }

            ItemStack newArmorItem = null;
            if (isNewVisual(originalClothesItem, newClothes)) {
                boolean sendMessages = newClothesMessages.add(newLevel);
                newArmorItem = handleNewClothesVisual(player, newClothesItem, sendMessages, placeholders, replacers);
            } else {
                newArmorItem = originalClothesItem.getFinalItem();
            }

            newArmorItems[i] = newArmorItem;
            bonus += newClothesItem.getTotalBonus();
        }

        setPlayerArmor(player, newArmorItems);
        event.setXpAmount(xpAmount * bonus);
    }

    private void handleNewClothesSounds(Player player) {
        SoundProperties soundProperties = Sounds.ARMOR_UP;
        if (soundProperties != null && soundProperties.isEnabled()) {
            player.playSound(player.getLocation(), soundProperties.getSound(), soundProperties.getVolume(), soundProperties.getPitch());
        }
    }

    private void handleNewLevelInfoMethods(LevelInfo newLevelInfo, String[] placeholders, String[] replacers) {
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
    }

    private ItemStack handleNewClothesVisual(Player player, ClothesItem newClothesItem, boolean sendMessages, String[] placeholders, String[] replacers) {
        newClothesItem.cache();
        ItemStack newArmorItem;
        newArmorItem = newClothesItem.getFinalItem();
        if (sendMessages) {
            sendMessages(player, Messages.NEW_CLOTHES, placeholders, replacers);
        }

        return newArmorItem;
    }

    private void setPlayerArmor(Player player, ItemStack[] newArmorItems) {
        player.getInventory().setArmorContents(newArmorItems);
        player.updateInventory();
    }

    private boolean isNewVisual(ClothesItem clothesItem, Clothes newClothes) {
        return !newClothes.equals(clothesItem.getClothes());
    }

    private boolean isNewLevel(int oldLevel, int newLevel) {
        return oldLevel != newLevel;
    }

    private void sendMessages(Player player, List<String> messages, String[] placeholders, String[] replacers) {
        for (String message : messages) {
            sendMessage(player, StringUtils.replaceEach(message, placeholders, replacers));
        }
    }

    private void sendMessages(Player player, List<String> messages) {
        for (String message : messages) {
            sendMessage(player, message);
        }
    }

    private void sendMessage(Player player, String message) {
        player.sendMessage(message);
    }
}