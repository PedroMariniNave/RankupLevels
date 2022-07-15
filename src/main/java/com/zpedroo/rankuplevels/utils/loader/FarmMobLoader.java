package com.zpedroo.rankuplevels.utils.loader;

import com.zpedroo.rankuplevels.objects.general.FarmMob;
import com.zpedroo.rankuplevels.utils.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class FarmMobLoader {

    @NotNull
    public static Map<EntityType, FarmMob> load(String where) {
        FileUtils.Files file = FileUtils.Files.EXPERIENCE;
        Map<EntityType, FarmMob> ret = new HashMap<>(4);
        for (String mobName : FileUtils.get().getSection(file, where)) {
            FarmMob farmMob = buildFarmMob(where, mobName, file);
            if (farmMob == null) continue;

            EntityType entityType = farmMob.getEntityType();
            ret.put(entityType, farmMob);
        }

        return ret;
    }

    @Nullable
    private static FarmMob buildFarmMob(String where, String mobName, FileUtils.Files file) {
        EntityType entityType = getEntityTypeByName(mobName);
        if (entityType == null) return null;

        double expAmount = FileUtils.get().getDouble(file, where + "." + mobName);

        return new FarmMob(entityType, expAmount);
    }

    @Nullable
    private static EntityType getEntityTypeByName(String name) {
        for (EntityType entityType : EntityType.values()) {
            if (StringUtils.equalsIgnoreCase(entityType.name(), name)) return entityType;
        }

        return null;
    }
}