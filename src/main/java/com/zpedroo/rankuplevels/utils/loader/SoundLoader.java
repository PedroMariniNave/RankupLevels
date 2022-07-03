package com.zpedroo.rankuplevels.utils.loader;

import com.zpedroo.rankuplevels.objects.properties.SoundProperties;
import com.zpedroo.rankuplevels.utils.FileUtils;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;

public class SoundLoader {

    @Nullable
    public static SoundProperties load(String soundName) {
        FileUtils.Files file = FileUtils.Files.CONFIG;
        if (!FileUtils.get().getFile(file).get().contains("Sounds." + soundName)) return null;

        Sound sound = Sound.valueOf(FileUtils.get().getString(file, "Sounds." + soundName + ".sound"));
        float volume = FileUtils.get().getFloat(file, "Sounds." + soundName + ".volume");
        float pitch = FileUtils.get().getFloat(file, "Sounds." + soundName + ".pitch");
        boolean enabled = FileUtils.get().getBoolean(file, "Sounds." + soundName + ".enabled");

        return new SoundProperties(sound, volume, pitch, enabled);
    }
}