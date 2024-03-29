package com.zpedroo.rankuplevels.listeners;

import com.zpedroo.rankuplevels.api.RankupLevelsAPI;
import com.zpedroo.rankuplevels.objects.general.FarmMob;
import com.zpedroo.rankuplevels.objects.properties.BlockProperties;
import com.zpedroo.rankuplevels.utils.config.Settings;
import com.zpedroo.rankuplevels.utils.experience.BlockBreakEXP;
import com.zpedroo.rankuplevels.utils.experience.DamageMobsEXP;
import com.zpedroo.rankuplevels.utils.experience.EnchantmentTableEXP;
import com.zpedroo.rankuplevels.utils.experience.KillMobsEXP;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigInteger;

public class EXPListeners implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMobDamage(EntityDamageByEntityEvent event) {
        if (!DamageMobsEXP.ENABLED) return;
        if (!(event.getDamager() instanceof Player) || event.isCancelled()) return;

        EntityType entityType = event.getEntity().getType();
        FarmMob farmMob = DamageMobsEXP.MOBS.get(entityType);
        if (farmMob == null) return;

        Player player = (Player) event.getDamager();
        double expToGive = farmMob.getExpAmount();
        ItemStack item = player.getItemInHand();
        if (DamageMobsEXP.LOOTING_ENABLED && isValidItem(item)) {
            ItemMeta meta = item.getItemMeta();
            int enchantmentLevel = meta.getEnchantLevel(Enchantment.LOOT_BONUS_MOBS);
            double multiplier = 1 + enchantmentLevel * DamageMobsEXP.LOOTING_MULTIPLIER;
            expToGive *= multiplier;
        }

        RankupLevelsAPI.addExp(player, expToGive);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMobKill(EntityDeathEvent event) {
        if (!KillMobsEXP.ENABLED) return;

        LivingEntity entity = event.getEntity();
        Player player = getKiller(entity);
        if (player == null) return;

        EntityType entityType = event.getEntity().getType();
        FarmMob farmMob = KillMobsEXP.MOBS.get(entityType);
        if (farmMob == null) return;

        BigInteger stackAmount = getStackAmount(entity);
        double expToGive = farmMob.getExpAmount() * stackAmount.doubleValue();
        ItemStack item = player.getItemInHand();
        if (KillMobsEXP.LOOTING_ENABLED && isValidItem(item)) {
            int enchantmentLevel = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
            double multiplier = 1 + (enchantmentLevel * KillMobsEXP.LOOTING_MULTIPLIER);
            expToGive *= multiplier;
        }

        RankupLevelsAPI.addExp(player, expToGive);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!BlockBreakEXP.ENABLED || event.isCancelled()) return;

        Player player = event.getPlayer();
        if (BlockBreakEXP.DISABLED_WORLDS.contains(player.getWorld().getName())) return;

        Block block = event.getBlock();
        if (!hasBlockProperties(block)) return;

        BlockProperties blockProperties = getBlockProperties(block);
        double expToGive = blockProperties.getExpAmount();

        ItemStack item = player.getItemInHand();
        if (BlockBreakEXP.FORTUNE_ENABLED && isValidItem(item)) {
            int enchantmentLevel = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
            double multiplier = 1 + (enchantmentLevel * BlockBreakEXP.FORTUNE_MULTIPLIER);
            expToGive *= multiplier;
        }

        RankupLevelsAPI.addExp(player, expToGive);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEnchant(EnchantItemEvent event) {
        if (!EnchantmentTableEXP.ENABLED || event.isCancelled()) return;

        Player player = event.getEnchanter();
        int levelCost = event.getExpLevelCost();
        double xpToGive = levelCost * EnchantmentTableEXP.EXP_PER_LEVEL;

        RankupLevelsAPI.addExp(player, xpToGive);
    }

    private Player getKiller(LivingEntity entity) {
        Player killer = entity.getKiller();
        if (killer == null) {
            if (!entity.hasMetadata(Settings.KILLER_METADATA)) return null;

            killer = (Player) entity.getMetadata(Settings.KILLER_METADATA).get(0).value();
        }

        return killer;
    }

    private BigInteger getStackAmount(Entity entity) {
        if (!entity.hasMetadata(Settings.STACK_AMOUNT_METADATA)) return BigInteger.ONE;

        return new BigInteger(entity.getMetadata(Settings.STACK_AMOUNT_METADATA).get(0).asString());
    }

    @Nullable
    private BlockProperties getBlockProperties(Block block) {
        return BlockBreakEXP.BLOCKS.stream().filter(blockProperties -> blockProperties.getMaterial().equals(block.getType())).findAny().orElse(null);
    }

    private boolean hasBlockProperties(Block block) {
        return getBlockProperties(block) != null;
    }

    private boolean isValidItem(ItemStack item) {
        return item != null && !item.getType().equals(Material.AIR);
    }
}