package com.zpedroo.rankuplevels.api.events;

import com.zpedroo.rankuplevels.objects.LevelInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@EqualsAndHashCode(callSuper = true)
@Data
public class PlayerUpgradeLevelEvent extends Event {

    private final Player player;
    private final int oldLevel;
    private final int newLevel;
    private final LevelInfo oldLevelInfo;
    private final LevelInfo newLevelInfo;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}