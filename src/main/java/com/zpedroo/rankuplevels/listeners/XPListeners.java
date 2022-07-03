package com.zpedroo.rankuplevels.listeners;

import com.zpedroo.rankuplevels.api.RankupLevelsAPI;
import com.zpedroo.rankuplevels.utils.experience.EnchantmentTableXP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

public class XPListeners implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEnchant(EnchantItemEvent event) {
        if (!EnchantmentTableXP.ENABLED || event.isCancelled()) return;

        Player player = event.getEnchanter();
        int levelCost = event.getExpLevelCost();
        double xpToGive = levelCost * EnchantmentTableXP.XP_PER_LEVEL;

        RankupLevelsAPI.addExp(player, xpToGive);
    }
}