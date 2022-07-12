package com.zpedroo.rankuplevels.utils.experience;

import com.zpedroo.rankuplevels.objects.properties.BlockProperties;
import com.zpedroo.rankuplevels.utils.FileUtils;
import com.zpedroo.rankuplevels.utils.loader.BlockLoader;

import java.util.List;

public class BlockBreakEXP {

    public static final boolean ENABLED = FileUtils.get().getBoolean(FileUtils.Files.EXPERIENCE, "Block-Break.enabled");

    public static final boolean FORTUNE_ENABLED = FileUtils.get().getBoolean(FileUtils.Files.EXPERIENCE, "Block-Break.fortune.enabled");

    public static final double FORTUNE_MULTIPLIER = FileUtils.get().getDouble(FileUtils.Files.EXPERIENCE, "Block-Break.fortune.multiplier");

    public static final List<String> DISABLED_WORLDS = FileUtils.get().getStringList(FileUtils.Files.EXPERIENCE, "Block-Break.disabled-worlds");

    public static final List<BlockProperties> BLOCKS = BlockLoader.load(FileUtils.get().getStringList(FileUtils.Files.EXPERIENCE, "Block-Break.blocks"));
}