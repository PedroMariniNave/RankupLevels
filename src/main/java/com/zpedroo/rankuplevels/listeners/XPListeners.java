package com.zpedroo.rankuplevels.listeners;

import com.zpedroo.rankuplevels.api.RankupLevelsAPI;
import com.zpedroo.rankuplevels.objects.properties.BlockProperties;
import com.zpedroo.rankuplevels.utils.experience.BlockBreakXP;
import com.zpedroo.rankuplevels.utils.experience.EnchantmentTableXP;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class XPListeners implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!BlockBreakXP.ENABLED || event.isCancelled()) return;

        Player player = event.getPlayer();
        if (BlockBreakXP.DISABLED_WORLDS.contains(player.getWorld().getName())) return;

        Block block = event.getBlock();
        if (!hasBlockProperties(block)) return;

        BlockProperties blockProperties = getBlockProperties(block);
        double xpToGive = blockProperties.getXp();

        ItemStack item = player.getItemInHand();
        if (BlockBreakXP.FORTUNE_ENABLED && isValidItem(item)) {
            ItemMeta meta = item.getItemMeta();
            int enchantmentLevel = meta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS);
            double fortuneMultiplier = enchantmentLevel > 0 ? (enchantmentLevel * BlockBreakXP.FORTUNE_MULTIPLIER) : 1;

            xpToGive *= fortuneMultiplier;
        }

        RankupLevelsAPI.addExp(player, xpToGive);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEnchant(EnchantItemEvent event) {
        if (!EnchantmentTableXP.ENABLED || event.isCancelled()) return;

        Player player = event.getEnchanter();
        int levelCost = event.getExpLevelCost();
        double xpToGive = levelCost * EnchantmentTableXP.XP_PER_LEVEL;

        RankupLevelsAPI.addExp(player, xpToGive);
    }

    @Nullable
    private BlockProperties getBlockProperties(Block block) {
        return BlockBreakXP.BLOCKS.stream().filter(blockProperties -> blockProperties.getMaterial().equals(block.getType())).findAny().orElse(null);
    }

    private boolean hasBlockProperties(Block block) {
        return getBlockProperties(block) != null;
    }

    private boolean isValidItem(ItemStack item) {
        return item != null && !item.getType().equals(Material.AIR);
    }
}