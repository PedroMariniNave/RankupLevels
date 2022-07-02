package com.zpedroo.rankuplevels.utils.progress;

import com.google.common.base.Strings;
import com.zpedroo.rankuplevels.utils.formula.ExperienceManager;

import static com.zpedroo.rankuplevels.utils.config.Progress.*;

public class ProgressConverter {

    public static String convert(double experience) {
        double percentage = getPercentage(experience) / 100;
        int completedProgressBars = (int) (DISPLAY_AMOUNT * percentage);
        int incompleteProgressBars = DISPLAY_AMOUNT - completedProgressBars;

        return Strings.repeat(COMPLETE_COLOR + SYMBOL, completedProgressBars) +
                Strings.repeat(INCOMPLETE_COLOR + SYMBOL, incompleteProgressBars);
    }

    public static double getPercentage(double experience) {
        int level = ExperienceManager.getLevel(experience);
        double xpToActualLevel = ExperienceManager.getFullLevelExperience(level-1);
        double xpToNextLevel = ExperienceManager.getFullLevelExperience(level);

        double requiredXPToUpgradeLevel = xpToNextLevel - xpToActualLevel;
        double has = experience - xpToActualLevel;

        double percentage = (has / requiredXPToUpgradeLevel) * 100;

        return percentage > 0 ? percentage : 0;
    }
}