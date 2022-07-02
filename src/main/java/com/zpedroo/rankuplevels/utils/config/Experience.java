package com.zpedroo.rankuplevels.utils.config;

import com.zpedroo.rankuplevels.utils.FileUtils;

public class Experience {

    public static final double BASE_EXP = FileUtils.get().getDouble(FileUtils.Files.CONFIG, "Experience-Formula.base-exp");

    public static final double EXPONENT = FileUtils.get().getDouble(FileUtils.Files.CONFIG, "Experience-Formula.exponent");
}