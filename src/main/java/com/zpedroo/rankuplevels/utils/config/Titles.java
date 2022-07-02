package com.zpedroo.rankuplevels.utils.config;

import com.zpedroo.rankuplevels.utils.FileUtils;
import com.zpedroo.rankuplevels.utils.color.Colorize;

public class Titles {

    public static final String[] UPGRADE = new String[]{
            Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Titles.upgrade.title")),
            Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Titles.upgrade.subtitle"))
    };
}