package com.zpedroo.rankuplevels.managers.cache;

import com.zpedroo.rankuplevels.RankupLevels;
import com.zpedroo.rankuplevels.mysql.DBConnection;
import com.zpedroo.rankuplevels.objects.Clothes;
import com.zpedroo.rankuplevels.objects.PlayerData;
import com.zpedroo.rankuplevels.objects.TagInfo;
import com.zpedroo.rankuplevels.utils.FileUtils;
import com.zpedroo.rankuplevels.utils.builder.ItemBuilder;
import com.zpedroo.rankuplevels.utils.color.Colorize;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

@Getter
@Setter
public class DataCache {

    private final Map<UUID, PlayerData> playersData = new HashMap<>(256);
    private final List<TagInfo> tags = getTagsFromConfig();
    private final List<Clothes> clothes = getClothesFromFolder();
    private List<PlayerData> topRanks = null;

    public DataCache() {
        RankupLevels.get().getServer().getScheduler().runTaskLaterAsynchronously(RankupLevels.get(), () -> {
            topRanks = DBConnection.getInstance().getDBManager().getTopRankFromDatabase();
        }, 100L);
    }

    private List<TagInfo> getTagsFromConfig() {
        FileUtils.Files file = FileUtils.Files.CONFIG;
        List<TagInfo> ret = new LinkedList<>();
        for (String str : FileUtils.get().getSection(file , "Settings.tags")) {
            String[] split = str.split("-");
            if (split.length <= 1) continue;

            int minLevel = Integer.parseInt(split[0]);
            int maxLevel = Integer.parseInt(split[1]);
            String tag = Colorize.getColored(FileUtils.get().getString(file, "Settings.tags." + str));

            ret.add(new TagInfo(minLevel, maxLevel, tag));
        }

        return ret;
    }

    private List<Clothes> getClothesFromFolder() {
        File folder = new File(RankupLevels.get().getDataFolder(), "/clothes");
        File[] files = folder.listFiles((file, name) -> name.endsWith(".yml"));
        List<Clothes> ret = new ArrayList<>(files == null ? 0 : files.length);
        if (files == null) return ret;

        for (File file : files) {
            FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);

            int requiredLevel = fileConfig.getInt("Clothes-Settings.required-level", 1);
            double bonus = fileConfig.getDouble("Clothes-Settings.bonus", 0);
            ItemStack[] items = new ItemStack[4];
            int index = 0;
            for (String str : fileConfig.getConfigurationSection("Clothes-Settings.items").getKeys(false)) {
                ItemStack item = ItemBuilder.build(fileConfig, "Clothes-Settings.items." + str).build();
                items[index++] = item;
            }

            ret.add(new Clothes(requiredLevel, bonus, items));
        }

        return ret;
    }
}