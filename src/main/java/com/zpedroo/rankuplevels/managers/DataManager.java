package com.zpedroo.rankuplevels.managers;

import com.zpedroo.rankuplevels.managers.cache.DataCache;
import com.zpedroo.rankuplevels.mysql.DBConnection;
import com.zpedroo.rankuplevels.objects.Clothes;
import com.zpedroo.rankuplevels.objects.ClothesItem;
import com.zpedroo.rankuplevels.objects.LevelInfo;
import com.zpedroo.rankuplevels.objects.PlayerData;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DataManager {

    private static DataManager instance;
    public static DataManager getInstance() { return instance; }

    private final DataCache dataCache = new DataCache();

    public DataManager() {
        instance = this;
    }

    @NotNull
    public PlayerData getPlayerData(@NotNull Player player) {
        return getPlayerData(player.getUniqueId());
    }

    @NotNull
    public PlayerData getPlayerData(@NotNull UUID uuid) {
        PlayerData data = dataCache.getPlayersData().get(uuid);
        if (data == null) {
            data = DBConnection.getInstance().getDBManager().getPlayerDataFromDatabase(uuid);
            dataCache.getPlayersData().put(uuid, data);
        }

        return data;
    }

    @Nullable
    public LevelInfo getLevelInfo(int level) {
        for (LevelInfo tagInfo : dataCache.getLevelsInfo()) {
            if (level >= tagInfo.getMinLevel() && level <= tagInfo.getMaxLevel()) return tagInfo;
        }

        return null;
    }

    @Nullable
    public Clothes getClothesByLevel(int level) {
        Clothes clothesFound = null;
        for (Clothes clothes : dataCache.getClothes().values()) {
            if (level >= clothes.getRequiredLevel()) {
                if (clothesFound == null || clothes.getRequiredLevel() > clothesFound.getRequiredLevel()) {
                    clothesFound = clothes;
                }
            }
        }

        return clothesFound;
    }

    @Nullable
    public Clothes getClothesByName(String name) {
        return dataCache.getClothes().get(name);
    }

    @Nullable
    public ClothesItem getClothesItem(@NotNull ItemStack item) {
        NBTItem nbt = new NBTItem(item);
        if (!nbt.hasKey("ClothesItemId")) return null;

        UUID id = nbt.getObject("ClothesItemId", UUID.class);
        ClothesItem clothesItem = dataCache.getClothesItem().get(id);
        if (clothesItem == null) {
            String name = nbt.getString("ClothesName");
            double experience = nbt.getDouble("ClothesExperience");
            Clothes clothes = getClothesByName(name);
            if (clothes == null) return null;

            ItemStack defaultItem = clothes.getSimilarItem(item);
            if (defaultItem == null) return null;

            clothesItem = new ClothesItem(clothes, defaultItem, experience);
            clothesItem.cache();
        }

        return clothesItem;
    }

    public void savePlayerData(@NotNull Player player) {
        savePlayerData(player.getUniqueId());
    }

    public void savePlayerData(@NotNull UUID uuid) {
        PlayerData data = dataCache.getPlayersData().get(uuid);
        savePlayerData(data);
    }

    public void savePlayerData(@Nullable PlayerData data) {
        if (data == null || !data.isQueueUpdate()) return;

        DBConnection.getInstance().getDBManager().savePlayerData(data);
        data.setUpdate(false);
    }

    public void saveAllPlayersData() {
        dataCache.getPlayersData().keySet().forEach(this::savePlayerData);
    }

    public void updateTopRanks() {
        dataCache.setTopRanks(DBConnection.getInstance().getDBManager().getTopRankFromDatabase());
    }

    public DataCache getCache() {
        return dataCache;
    }
}