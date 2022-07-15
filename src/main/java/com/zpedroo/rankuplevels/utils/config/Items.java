package com.zpedroo.rankuplevels.utils.config;

import com.zpedroo.rankuplevels.utils.FileUtils;
import com.zpedroo.rankuplevels.utils.builder.ItemBuilder;
import de.tr7zw.nbtapi.NBTItem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Items {

    private static final ItemStack EXP_PERCENTAGE_ITEM = ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.CONFIG).get(), "Exp-Percentage-Item").build();

    @NotNull
    public static ItemStack getExpPercentageItem(int percentage) {
        NBTItem nbt = new NBTItem(EXP_PERCENTAGE_ITEM.clone());
        nbt.setInteger("ExpPercentage", percentage);

        String[] placeholders = new String[]{
                "{percentage}"
        };
        String[] replacers = new String[]{
                String.valueOf(percentage)
        };

        return replaceItemPlaceholders(nbt.getItem(), placeholders, replacers);
    }

    @NotNull
    private static ItemStack replaceItemPlaceholders(ItemStack item, String[] placeholders, String[] replacers) {
        if (item.getItemMeta() != null) {
            String displayName = item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : null;
            List<String> lore = item.getItemMeta().hasLore() ? item.getItemMeta().getLore() : null;
            ItemMeta meta = item.getItemMeta();
            if (displayName != null) meta.setDisplayName(StringUtils.replaceEach(displayName, placeholders, replacers));
            if (lore != null) {
                List<String> newLore = new ArrayList<>(lore.size());
                for (String str : lore) {
                    newLore.add(StringUtils.replaceEach(str, placeholders, replacers));
                }

                meta.setLore(newLore);
            }

            item.setItemMeta(meta);
        }

        return item;
    }
}