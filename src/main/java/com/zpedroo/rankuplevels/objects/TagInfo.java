package com.zpedroo.rankuplevels.objects;

import lombok.Data;

@Data
public class TagInfo {

    private final int minLevel;
    private final int maxLevel;
    private final String tag;
}