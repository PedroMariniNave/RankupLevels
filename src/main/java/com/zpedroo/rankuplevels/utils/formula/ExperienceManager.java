package com.zpedroo.rankuplevels.utils.formula;

import static com.zpedroo.rankuplevels.utils.config.Experience.*;

public class ExperienceManager {

    public static int getLevel(double experience) {
        int level = 1;
        while (experience >= getFullLevelExperience(level)) {
            ++level;
        }

        return level;
    }

    public static double getFullLevelExperience(int level) {
        double requiredXP = 0;
        for (int i = 1; i <= level; i++) {
            requiredXP += getUpgradeLevelExperience(i);
        }

        return requiredXP;
    }

    public static double getUpgradeLevelExperience(int level) {
        return getLevelExperience(++level);
    }

    public static double getLevelExperience(int level) {
        if (level <= 1) return 0;

        return Math.floor(BASE_EXP + (BASE_EXP * Math.pow(level, EXPONENT)));
    }

}