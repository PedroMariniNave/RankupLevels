package com.zpedroo.rankuplevels.listeners;

import com.zpedroo.rankuplevels.api.RankupLevelsAPI;
import com.zpedroo.rankuplevels.enums.FormulaType;
import com.zpedroo.rankuplevels.utils.formula.ExperienceManager;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ItemActivateListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;
        if (event.getItem() == null || event.getItem().getType().equals(Material.AIR)) return;

        ItemStack item = event.getItem().clone();
        NBTItem nbt = new NBTItem(item);
        if (!nbt.hasKey("ExpPercentage")) return;

        event.setCancelled(true);

        double percentage = nbt.getInteger("ExpPercentage");
        if (percentage <= 0) return;

        Player player = event.getPlayer();
        int level = RankupLevelsAPI.getLevel(player);
        double playerExperience = RankupLevelsAPI.getExpAmount(player);
        double experienceToUpgrade = ExperienceManager.getFullLevelExperience(level, FormulaType.PLAYER_LEVEL);
        double remainingExperience = experienceToUpgrade - playerExperience;
        double expToGive = remainingExperience * percentage / 100;
        RankupLevelsAPI.addExp(player, expToGive);

        item.setAmount(1);
        player.getInventory().removeItem(item);
        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 0.5f, 10f);
    }
}
