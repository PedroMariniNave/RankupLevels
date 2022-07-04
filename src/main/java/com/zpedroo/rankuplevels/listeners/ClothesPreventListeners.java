package com.zpedroo.rankuplevels.listeners;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ClothesPreventListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemBreak(PlayerItemDamageEvent event) {
        if (isClothesItem(event.getItem())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEnchant(EnchantItemEvent event) {
        if (isClothesItem(event.getItem())) event.setCancelled(true);
    }

    private boolean isClothesItem(@NotNull ItemStack item) {
        NBTItem nbt = new NBTItem(item);
        return nbt.hasKey("ClothesItemId");
    }
}