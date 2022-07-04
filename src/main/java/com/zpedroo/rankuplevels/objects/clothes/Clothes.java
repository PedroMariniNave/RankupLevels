package com.zpedroo.rankuplevels.objects.clothes;

import lombok.Data;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

@Data
public class Clothes implements Serializable {

    private final String name;
    private final int requiredLevel;
    private final double bonusPerLevel;
    private final ItemStack[] items;

    public ItemStack[] getItems() {
        ItemStack[] ret = new ItemStack[items.length];
        int index = 0;
        for (ItemStack item : items) {
            ClothesItem clothesItem = new ClothesItem(this, item, 0);
            clothesItem.cache();
            ret[index++] = clothesItem.getFinalItem();
        }

        return ret;
    }

    @Nullable
    public ItemStack getSimilarItem(ItemStack itemToCompare) {
        for (ItemStack item : items) {
            if (item.getType().equals(itemToCompare.getType())) return item;
        }

        return null;
    }
}