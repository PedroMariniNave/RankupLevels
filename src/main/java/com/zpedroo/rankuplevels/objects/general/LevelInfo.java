package com.zpedroo.rankuplevels.objects.general;

import lombok.Data;

import java.util.List;

@Data
public class LevelInfo {

    private final int minLevel;
    private final int maxLevel;
    private final String tag;
    private final String actionBar;
    private final List<String> messages;
    private final List<String> upgradeCommands;
}