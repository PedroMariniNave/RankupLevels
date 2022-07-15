package com.zpedroo.rankuplevels.utils.experience;

import com.zpedroo.rankuplevels.objects.general.FarmMob;
import com.zpedroo.rankuplevels.utils.FileUtils;
import com.zpedroo.rankuplevels.utils.loader.FarmMobLoader;
import org.bukkit.entity.EntityType;

import java.util.Map;

public class DamageMobsEXP {

    public static final boolean ENABLED = FileUtils.get().getBoolean(FileUtils.Files.EXPERIENCE, "Damage-Mobs.enabled");

    public static final boolean LOOTING_ENABLED = FileUtils.get().getBoolean(FileUtils.Files.EXPERIENCE, "Damage-Mobs.looting.enabled");

    public static final double LOOTING_MULTIPLIER = FileUtils.get().getDouble(FileUtils.Files.EXPERIENCE, "Damage-Mobs.looting.multiplier");

    public static final Map<EntityType, FarmMob> MOBS = FarmMobLoader.load("Damage-Mobs.mobs");
}