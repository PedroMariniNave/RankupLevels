package com.zpedroo.rankuplevels.objects;

import lombok.Data;
import org.bukkit.Sound;

@Data
public class SoundProperties {

    private final Sound sound;
    private final float volume;
    private final float pitch;
    private final boolean enabled;
}