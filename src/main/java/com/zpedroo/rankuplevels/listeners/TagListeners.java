package com.zpedroo.rankuplevels.listeners;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import com.zpedroo.rankuplevels.managers.DataManager;
import com.zpedroo.rankuplevels.objects.general.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class TagListeners implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(ChatMessageEvent event) {
        PlayerData data = DataManager.getInstance().getPlayerData(event.getSender());
        event.setTagValue("level", data.getReplacedLevelTag());
    }
}