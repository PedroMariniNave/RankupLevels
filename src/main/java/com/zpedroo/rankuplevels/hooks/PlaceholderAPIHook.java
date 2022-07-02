package com.zpedroo.rankuplevels.hooks;

import com.zpedroo.rankuplevels.managers.DataManager;
import com.zpedroo.rankuplevels.objects.PlayerData;
import com.zpedroo.rankuplevels.utils.formatter.NumberFormatter;
import com.zpedroo.rankuplevels.utils.progress.ProgressConverter;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    private final Plugin plugin;

    public PlaceholderAPIHook(Plugin plugin) {
        this.plugin = plugin;
    }

    @NotNull
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @NotNull
    public String getIdentifier() {
        return "rankup";
    }

    @NotNull
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    public String onPlaceholderRequest(@NotNull Player player, @NotNull String identifier) {
        PlayerData data = DataManager.getInstance().getPlayerData(player);
        switch (identifier.toUpperCase()) {
            case "LEVEL":
                return NumberFormatter.getInstance().formatThousand(data.getLevel());
            case "LEVEL_TAG":
                return data.getReplacedLevelTag();
            case "PROGRESS_BAR":
                return ProgressConverter.convert(data.getExpAmount());
            case "PERCENTAGE":
                return NumberFormatter.getInstance().formatDecimal(ProgressConverter.getPercentage(data.getExpAmount()));
            case "EXP":
                return NumberFormatter.getInstance().format(data.getExpAmount());
        }

        return null;
    }
}