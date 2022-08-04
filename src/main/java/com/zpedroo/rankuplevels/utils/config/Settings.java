package com.zpedroo.rankuplevels.utils.config;

import com.zpedroo.rankuplevels.utils.FileUtils;

import java.util.List;

public class Settings {

    public static final String LEVELS_COMMAND = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.commands.levels.command");

    public static final List<String> LEVELS_ALIASES = FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Settings.commands.levels.aliases");

    public static final String CLOTHES_COMMAND = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.commands.clothes.command");

    public static final List<String> CLOTHES_ALIASES = FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Settings.commands.clothes.aliases");

    public static final String ADMIN_PERMISSION = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.admin-permission");

    public static final long DATA_UPDATE_INTERVAL = FileUtils.get().getLong(FileUtils.Files.CONFIG, "Settings.data-update-interval");

    public static final String RANK_PERMISSION = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.rank-permission");

    public static final boolean ADD_LOWER_RANKS_PERMISSION = FileUtils.get().getBoolean(FileUtils.Files.CONFIG, "Settings.add-lower-ranks-permission");

    public static final int CLOTHES_PICKUP_COOLDOWN = FileUtils.get().getInt(FileUtils.Files.CONFIG, "Settings.clothes-pickup-cooldown");

    public static final int DEFAULT_LEVEL = FileUtils.get().getInt(FileUtils.Files.CONFIG, "Settings.default-level");

    public static final int MAX_LEVEL = FileUtils.get().getInt(FileUtils.Files.CONFIG, "Settings.max-level");

    public static final int PROGRESS_DIGITS = FileUtils.get().getInt(FileUtils.Files.CONFIG, "Settings.progress-digits");

    public static final int CLOTHES_DIGITS = FileUtils.get().getInt(FileUtils.Files.CONFIG, "Settings.clothes-digits");

    public static final String STACK_AMOUNT_METADATA = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.stack-amount-metadata");

    public static final String KILLER_METADATA = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.killer-metadata");
}