package com.zpedroo.rankuplevels.objects.clothes;

import com.zpedroo.rankuplevels.enums.FormulaType;
import com.zpedroo.rankuplevels.managers.DataManager;
import com.zpedroo.rankuplevels.utils.formatter.NumberFormatter;
import com.zpedroo.rankuplevels.utils.formula.ExperienceManager;
import com.zpedroo.rankuplevels.utils.progress.ProgressConverter;
import de.tr7zw.nbtapi.NBTItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.Serializable;
import java.util.*;

import static com.zpedroo.rankuplevels.utils.config.Settings.CLOTHES_DIGITS;

@Getter
@Setter
public class ClothesItem implements Serializable {

    private final UUID id = UUID.randomUUID();
    private final Clothes clothes;
    private final ItemStack defaultItem;
    private double experience;

    @Getter(AccessLevel.NONE)
    private int levelCache = -1;

    @Getter(AccessLevel.NONE)
    private Map<Integer, Double> oldClothesBonusCache = null;

    public ClothesItem(Clothes clothes, ItemStack defaultItem, double experience) {
        this.clothes = clothes;
        this.defaultItem = defaultItem;
        this.experience = experience;
        this.updateCache();
    }

    public int getLevel() {
        return levelCache;
    }

    public double getProgress() {
        return ProgressConverter.getPercentage(experience, FormulaType.CLOTHES_LEVEL);
    }

    public double getTotalBonus() {
        double bonus = clothes.getBonusPerLevel();
        int minLevel = clothes.getRequiredLevel();
        int level = getLevel();
        int levelDifference = level - minLevel;
        double actualClothesBonus = bonus * levelDifference;

        return getOldClothesBonuses() + actualClothesBonus;
    }

    public double getOldClothesBonuses() {
        return oldClothesBonusCache.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public String getProgressDisplay() {
        return ProgressConverter.convert(experience, FormulaType.CLOTHES_LEVEL);
    }

    public void addExperience(double amount) {
        this.experience += amount;
        if (levelCache != calcLevel()) updateCache();
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
        double bonus = getTotalBonus();
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
                    "{percentage}",
                    "{progress}"
            };
            String[] replacers = new String[] {
                    NumberFormatter.getInstance().formatThousand(level),
                    NumberFormatter.getInstance().formatDecimal(bonus, CLOTHES_DIGITS),
                    NumberFormatter.getInstance().formatDecimal(progress, 2),
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

    private void updateCache() {
        this.levelCache = calcLevel();
        this.oldClothesBonusCache = calcOldClothesBonuses();
    }

    private int calcLevel() {
        return ExperienceManager.getLevel(experience, FormulaType.CLOTHES_LEVEL);
    }

    private Map<Integer, Double> calcOldClothesBonuses() {
        Map<Integer, Double> ret = oldClothesBonusCache == null ? new HashMap<>() : oldClothesBonusCache;

        int minLevel = clothes.getRequiredLevel();
        int levelToGet = ret.isEmpty() ? 1 : Collections.max(ret.keySet());
        while (levelToGet <= minLevel) {
            Clothes clothes = DataManager.getInstance().getClothesByLevel(levelToGet++);
            if (clothes == null) continue;

            double bonus = clothes.getBonusPerLevel();
            ret.put(levelToGet, bonus);
        }

        return ret;
    }

    public void cache() {
        DataManager.getInstance().getCache().getClothesItem().put(id, this);
    }
}