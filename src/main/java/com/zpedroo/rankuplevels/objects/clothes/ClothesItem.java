package com.zpedroo.rankuplevels.objects.clothes;

import com.zpedroo.rankuplevels.enums.FormulaType;
import com.zpedroo.rankuplevels.managers.DataManager;
import com.zpedroo.rankuplevels.utils.formatter.NumberFormatter;
import com.zpedroo.rankuplevels.utils.formula.ExperienceManager;
import com.zpedroo.rankuplevels.utils.progress.ProgressConverter;
import de.tr7zw.nbtapi.NBTItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.zpedroo.rankuplevels.utils.config.Settings.CLOTHES_DIGITS;

@Data
@AllArgsConstructor
public class ClothesItem implements Serializable {

    private final UUID id = UUID.randomUUID();
    private final Clothes clothes;
    private final ItemStack defaultItem;
    private double experience = 0;

    public int getLevel() {
        return ExperienceManager.getLevel(experience, FormulaType.CLOTHES_LEVEL);
    }

    public double getProgress() {
        return ProgressConverter.getPercentage(experience, FormulaType.CLOTHES_LEVEL);
    }

    public double getBonus() {
        return clothes.getBonus() * getLevel();
    }

    public String getProgressDisplay() {
        return ProgressConverter.convert(experience, FormulaType.CLOTHES_LEVEL);
    }

    public void addExperience(double amount) {
        this.experience += amount;
    }

    public ItemStack getFinalItem() {
        NBTItem nbt = new NBTItem(getDefaultItem());
        nbt.setString("ClothesName", clothes.getName());
        nbt.setDouble("ClothesExperience", experience);
        nbt.setObject("ClothesItemId", id);

        ItemStack item = nbt.getItem();
        ItemMeta meta = item.getItemMeta();
        int level = getLevel();
        String progressDisplay = getProgressDisplay();
        double bonus = getBonus();
        double progress = getProgress();
        float maxDurability = item.getType().getMaxDurability();
        float durabilityPerPercentage = maxDurability / 100;

        item.setDurability((short) (maxDurability - (durabilityPerPercentage * progress)));

        if (meta != null) {
            String displayName = meta.hasDisplayName() ? meta.getDisplayName() : null;
            List<String> lore = meta.hasLore() ? meta.getLore() : null;
            String[] placeholders = new String[] {
                    "{level}",
                    "{bonus}",
                    "{progress}"
            };
            String[] replacers = new String[] {
                    NumberFormatter.getInstance().formatThousand(level),
                    NumberFormatter.getInstance().formatDecimal(bonus, CLOTHES_DIGITS),
                    progressDisplay
            };

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

    public void cache() {
        DataManager.getInstance().getCache().getClothesItem().put(id, this);
    }
}