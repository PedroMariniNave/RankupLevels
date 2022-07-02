package com.zpedroo.rankuplevels.managers.cache;

import com.zpedroo.rankuplevels.RankupLevels;
import com.zpedroo.rankuplevels.mysql.DBConnection;
import com.zpedroo.rankuplevels.objects.PlayerData;
import com.zpedroo.rankuplevels.objects.TagInfo;
import com.zpedroo.rankuplevels.utils.FileUtils;
import com.zpedroo.rankuplevels.utils.color.Colorize;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class DataCache {

    private final Map<UUID, PlayerData> playersData = new HashMap<>(256);
    private final List<TagInfo> tags = getTagsFromConfig();
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
}