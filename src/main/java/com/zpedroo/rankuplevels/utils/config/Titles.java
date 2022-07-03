package com.zpedroo.rankuplevels.utils.config;

import com.zpedroo.rankuplevels.utils.FileUtils;
import com.zpedroo.rankuplevels.utils.color.Colorize;

public class Titles {

    public static final String[] RANK_UP = new String[]{
            Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Titles.rank-up.title")),
            Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Titles.rank-up.subtitle"))
    };
}