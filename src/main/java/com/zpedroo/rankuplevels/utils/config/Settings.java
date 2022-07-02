package com.zpedroo.rankuplevels.utils.config;

import com.zpedroo.rankuplevels.utils.FileUtils;

import java.util.List;

public class Settings {

    public static final String COMMAND = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.command");

    public static final List<String> ALIASES = FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Settings.aliases");

    public static final String ADMIN_PERMISSION = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.admin-permission");

    public static final long SAVE_INTERVAL = FileUtils.get().getLong(FileUtils.Files.CONFIG, "Settings.save-interval");

    public static final String RANK_PERMISSION = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.rank-permission");

    public static final boolean ADD_LOWER_RANKS_PERMISSION = FileUtils.get().getBoolean(FileUtils.Files.CONFIG, "Settings.add-lower-ranks-permission");

    public static final int MAX_LEVEL = FileUtils.get().getInt(FileUtils.Files.CONFIG, "Settings.max-level");

    public static final boolean THUNDER_ENABLED = FileUtils.get().getBoolean(FileUtils.Files.CONFIG, "Settings.thunder.enabled");

    public static final boolean DOUBLE_THUNDER_ENABLED = FileUtils.get().getBoolean(FileUtils.Files.CONFIG, "Settings.thunder.double");

    public static final boolean SILENT_THUNDER_ENABLED = FileUtils.get().getBoolean(FileUtils.Files.CONFIG, "Settings.thunder.silent");
}