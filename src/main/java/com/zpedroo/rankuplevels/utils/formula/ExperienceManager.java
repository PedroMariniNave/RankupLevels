package com.zpedroo.rankuplevels.utils.formula;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.zpedroo.rankuplevels.enums.FormulaType;

import static com.zpedroo.rankuplevels.utils.config.ExperienceFormula.*;
import static com.zpedroo.rankuplevels.utils.config.Settings.DEFAULT_LEVEL;

public class ExperienceManager {

    private static final Table<Integer, FormulaType, Double> levelExperienceCache = HashBasedTable.create();
    private static final Table<Integer, FormulaType, Double> levelFullExperienceCache = HashBasedTable.create();

    public static int getLevel(double experience, FormulaType type) {
        int level = 1;
        while (experience >= getFullLevelExperience(level, type)) {
            ++level;
        }

        return level;
    }

    public static double getFullLevelExperience(int level, FormulaType type) {
        if (!levelFullExperienceCache.contains(level, type)) {
            double requiredXP = 0;
            for (int i = 1; i <= level; i++) {
                requiredXP += getUpgradeLevelExperience(i, type);
            }

            levelFullExperienceCache.put(level, type, requiredXP);
        }

        return levelFullExperienceCache.get(level, type);
    }

    public static double getUpgradeLevelExperience(int level, FormulaType type) {
        return getLevelExperience(++level, type);
    }

    public static double getLevelExperience(int level, FormulaType type) {
        if (level <= DEFAULT_LEVEL) return 0;
        if (!levelExperienceCache.contains(level, type)) {
            double baseExp = type == FormulaType.PLAYER_LEVEL ? PLAYER_BASE_EXP : CLOTHES_BASE_EXP;
            double exponent = type == FormulaType.PLAYER_LEVEL ? PLAYER_EXPONENT : CLOTHES_EXPONENT;

            double experience = Math.floor(baseExp + (baseExp * Math.pow(level, exponent)));
            levelExperienceCache.put(level, type, experience);
        }

        return levelExperienceCache.get(level, type);
    }

}