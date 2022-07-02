package com.zpedroo.rankuplevels.objects;

import com.zpedroo.rankuplevels.RankupLevels;
import com.zpedroo.rankuplevels.api.events.PlayerUpgradeLevelEvent;
import com.zpedroo.rankuplevels.managers.DataManager;
import com.zpedroo.rankuplevels.utils.config.Settings;
import com.zpedroo.rankuplevels.utils.formula.ExperienceManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static com.zpedroo.rankuplevels.utils.config.Settings.MAX_LEVEL;

public class PlayerData {

    private final UUID uuid;
    private double expAmount;
    private int level = -1;
    private boolean update = false;

    public PlayerData(UUID uuid, double expAmount) {
        this.uuid = uuid;
        this.expAmount = expAmount;
        this.updateLevel();
    }

    public UUID getUniqueId() {
        return uuid;
    }

    @Nullable
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public double getExpAmount() {
        return expAmount;
    }

    public int getLevel() {
        return Math.min(level, MAX_LEVEL);
    }

    public String getReplacedLevelTag() {
        TagInfo tagInfo = DataManager.getInstance().getTagInfoByLevel(level);
        if (tagInfo == null) return "";

        return StringUtils.replace(tagInfo.getTag(), "{level}", String.valueOf(level));
    }

    public boolean isQueueUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public void addExp(double amount) {
        this.setExpAmount(expAmount + amount);
    }

    public void setExpAmount(double expAmount) {
        this.expAmount = expAmount;
        this.update = true;
        this.updateLevel();
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void updateLevel() {
        final int oldLevel = level;
        int newLevel = ExperienceManager.getLevel(expAmount);
        setLevel(newLevel);

        if (oldLevel == -1 || newLevel == oldLevel) return;

        Player player = getPlayer();
        updatePermissions();

        PlayerUpgradeLevelEvent event = new PlayerUpgradeLevelEvent(player, oldLevel, newLevel);
        Bukkit.getPluginManager().callEvent(event);
    }

    public void updatePermissions() {
        Player player = getPlayer();
        if (player == null) return;

        clearPermissions();
        List<String> permissionsToAdd = new LinkedList<>();
        int minLevel = Settings.ADD_LOWER_RANKS_PERMISSION ? 1 : level;
        for (int i = minLevel; i <= level; ++i) {
            permissionsToAdd.add(StringUtils.replace(Settings.RANK_PERMISSION, "{level}", String.valueOf(i)));
        }

        for (String permission : permissionsToAdd) {
            PermissionAttachment attachment = player.addAttachment(RankupLevels.get());
            attachment.setPermission(permission, true);
        }
    }

    public void clearPermissions() {
        Player player = getPlayer();
        if (player == null) return;

        for (PermissionAttachmentInfo attachment : player.getEffectivePermissions()) {
            PermissionAttachment permissionAttachment = attachment.getAttachment();
            if (permissionAttachment == null) continue;

            Plugin plugin = permissionAttachment.getPlugin();
            if (plugin == null || plugin != RankupLevels.get()) continue;

            player.removeAttachment(permissionAttachment);
        }
    }
}