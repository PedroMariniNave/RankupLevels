package com.zpedroo.rankuplevels.utils.progress;

import com.google.common.base.Strings;
import com.zpedroo.rankuplevels.enums.FormulaType;
import com.zpedroo.rankuplevels.utils.formula.ExperienceManager;

import static com.zpedroo.rankuplevels.utils.config.Progress.*;

public class ProgressConverter {

    public static String convert(double experience, FormulaType type) {
        double percentage = getPercentage(experience, type) / 100;
        int completedProgressBars = (int) (DISPLAY_AMOUNT * percentage);
        int incompleteProgressBars = DISPLAY_AMOUNT - completedProgressBars;

        return COMPLETE_COLOR + Strings.repeat(SYMBOL, completedProgressBars) +
                INCOMPLETE_COLOR + Strings.repeat(SYMBOL, incompleteProgressBars);
    }

    public static double getPercentage(double experience, FormulaType type) {
        int level = ExperienceManager.getLevel(experience, type);
        double xpToActualLevel = ExperienceManager.getFullLevelExperience(level-1, type);
        double xpToNextLevel = ExperienceManager.getFullLevelExperience(level, type);

        double requiredXPToUpgradeLevel = xpToNextLevel - xpToActualLevel;
        double has = experience - xpToActualLevel;

        double percentage = (has / requiredXPToUpgradeLevel) * 100;

        return percentage > 0 ? percentage : 0;
    }
}