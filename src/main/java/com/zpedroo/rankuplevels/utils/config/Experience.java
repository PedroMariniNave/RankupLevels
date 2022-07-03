package com.zpedroo.rankuplevels.utils.config;

import com.zpedroo.rankuplevels.utils.FileUtils;

public class Experience {

    public static final double PLAYER_BASE_EXP = FileUtils.get().getDouble(FileUtils.Files.CONFIG, "Experience-Formula.player-level.base-exp");

    public static final double PLAYER_EXPONENT = FileUtils.get().getDouble(FileUtils.Files.CONFIG, "Experience-Formula.player-level.exponent");

    public static final double CLOTHES_BASE_EXP = FileUtils.get().getDouble(FileUtils.Files.CONFIG, "Experience-Formula.clothes-level.base-exp");

    public static final double CLOTHES_EXPONENT = FileUtils.get().getDouble(FileUtils.Files.CONFIG, "Experience-Formula.clothes-level.exponent");
}