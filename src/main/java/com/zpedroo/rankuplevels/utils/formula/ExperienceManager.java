package com.zpedroo.rankuplevels.utils.formula;

import com.zpedroo.rankuplevels.enums.FormulaType;

import static com.zpedroo.rankuplevels.utils.config.Experience.*;
import static com.zpedroo.rankuplevels.utils.config.Settings.DEFAULT_LEVEL;

public class ExperienceManager {

    public static int getLevel(double experience, FormulaType type) {
        int level = 1;
        while (experience >= getFullLevelExperience(level, type)) {
            ++level;
        }

        return level;
    }

    public static double getFullLevelExperience(int level, FormulaType type) {
        double requiredXP = 0;
        for (int i = 1; i <= level; i++) {
            requiredXP += getUpgradeLevelExperience(i, type);
        }

        return requiredXP;
    }

    public static double getUpgradeLevelExperience(int level, FormulaType type) {
        return getLevelExperience(++level, type);
    }

    public static double getLevelExperience(int level, FormulaType type) {
        if (level <= DEFAULT_LEVEL) return 0;

        double baseExp = type == FormulaType.PLAYER_LEVEL ? PLAYER_BASE_EXP : CLOTHES_BASE_EXP;
        double exponent = type == FormulaType.PLAYER_LEVEL ? PLAYER_EXPONENT : CLOTHES_EXPONENT;

        return Math.floor(baseExp + (baseExp * Math.pow(level, exponent)));
    }

}