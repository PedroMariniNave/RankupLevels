package com.zpedroo.rankuplevels.utils.config;

import com.zpedroo.rankuplevels.utils.FileUtils;
import com.zpedroo.rankuplevels.utils.color.Colorize;

import java.util.List;

public class Messages {

    public static final List<String> NEW_LEVEL = Colorize.getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.new-level"));

    public static final List<String> NEW_CLOTHES = Colorize.getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.new-clothes"));

    public static final String COOLDOWN = Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.cooldown"));
}