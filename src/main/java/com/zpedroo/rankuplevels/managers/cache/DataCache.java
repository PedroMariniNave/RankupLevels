package com.zpedroo.rankuplevels.managers.cache;

import com.zpedroo.rankuplevels.RankupLevels;
import com.zpedroo.rankuplevels.mysql.DBConnection;
import com.zpedroo.rankuplevels.objects.Clothes;
import com.zpedroo.rankuplevels.objects.ClothesItem;
import com.zpedroo.rankuplevels.objects.PlayerData;
import com.zpedroo.rankuplevels.objects.LevelInfo;
import com.zpedroo.rankuplevels.utils.FileUtils;
import com.zpedroo.rankuplevels.utils.builder.ItemBuilder;
import com.zpedroo.rankuplevels.utils.color.Colorize;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

@Getter
@Setter
public class DataCache {

    private final Map<UUID, PlayerData> playersData = new HashMap<>(256);
    private final Map<UUID, ClothesItem> clothesItem = new HashMap<>(64);
    private final Map<String, Clothes> clothes = getClothesFromFolder();
    private final List<LevelInfo> levelsInfo = getLevelsFromConfig();
    private List<PlayerData> topRanks = null;

    public DataCache() {
        RankupLevels.get().getServer().getScheduler().runTaskLaterAsynchronously(RankupLevels.get(), () -> {
            topRanks = DBConnection.getInstance().getDBManager().getTopRankFromDatabase();
        }, 40L);
    }

    @NotNull
    private List<LevelInfo> getLevelsFromConfig() {
        FileUtils.Files file = FileUtils.Files.CONFIG;
        List<LevelInfo> ret = new LinkedList<>();
        for (String level : FileUtils.get().getSection(file , "Levels")) {
            String[] split = level.split("-");
            if (split.length <= 1) continue;

            int minLevel = Integer.parseInt(split[0]);
            int maxLevel = Integer.parseInt(split[1]);
            String tag = Colorize.getColored(FileUtils.get().getString(file, "Levels." + level + ".tag"));
            String actionBar = Colorize.getColored(FileUtils.get().getString(file, "Levels." + level + ".action-bar", null));
            List<String> messages = Colorize.getColored(FileUtils.get().getStringList(file, "Levels." + level + ".messages"));
            List<String> upgradeCommands = FileUtils.get().getStringList(file, "Levels." + level + ".commands");

            ret.add(new LevelInfo(minLevel, maxLevel, tag, actionBar, messages, upgradeCommands));
        }

        return ret;
    }

    @NotNull
    private Map<String, Clothes> getClothesFromFolder() {
        File folder = new File(RankupLevels.get().getDataFolder(), "/clothes");
        File[] files = folder.listFiles((file, name) -> name.endsWith(".yml"));
        Map<String, Clothes> ret = new HashMap<>(files == null ? 0 : files.length);
        if (files == null) return ret;

        for (File file : files) {
            FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);

            String name = file.getName().replace(".yml", "");
            int requiredLevel = fileConfig.getInt("Clothes-Settings.required-level", 1);
            double bonus = fileConfig.getDouble("Clothes-Settings.bonus", 0);
            ItemStack[] items = new ItemStack[4];
            int index = 0;
            for (String str : fileConfig.getConfigurationSection("Clothes-Settings.items").getKeys(false)) {
                ItemStack item = ItemBuilder.build(fileConfig, "Clothes-Settings.items." + str).build();
                items[index++] = item;
            }

            ret.put(name, new Clothes(name, requiredLevel, bonus, items));
        }

        return ret;
    }
}