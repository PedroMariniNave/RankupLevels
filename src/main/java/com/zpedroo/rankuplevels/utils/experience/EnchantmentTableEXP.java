package com.zpedroo.rankuplevels.utils.experience;

import com.zpedroo.rankuplevels.utils.FileUtils;

public class EnchantmentTableEXP {

    public static final boolean ENABLED = FileUtils.get().getBoolean(FileUtils.Files.EXPERIENCE, "Enchantment-Table.enabled");

    public static final double EXP_PER_LEVEL = FileUtils.get().getDouble(FileUtils.Files.EXPERIENCE, "Enchantment-Table.exp-per-level");
}