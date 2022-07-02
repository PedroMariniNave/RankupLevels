package com.zpedroo.rankuplevels.managers;

import com.zpedroo.rankuplevels.managers.cache.DataCache;
import com.zpedroo.rankuplevels.mysql.DBConnection;
import com.zpedroo.rankuplevels.objects.Clothes;
import com.zpedroo.rankuplevels.objects.PlayerData;
import com.zpedroo.rankuplevels.objects.LevelInfo;
import org.bukkit.entity.Player;
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
        for (Clothes clothes : dataCache.getClothes()) {
            if (level >= clothes.getRequiredLevel()) {
                if (clothesFound == null || clothes.getRequiredLevel() > clothesFound.getRequiredLevel()) {
                    clothesFound = clothes;
                }
            }
        }

        return clothesFound;
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