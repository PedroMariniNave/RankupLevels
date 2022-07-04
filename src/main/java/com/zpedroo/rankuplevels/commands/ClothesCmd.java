package com.zpedroo.rankuplevels.commands;

import com.zpedroo.rankuplevels.managers.DataManager;
import com.zpedroo.rankuplevels.objects.clothes.Clothes;
import com.zpedroo.rankuplevels.utils.config.Messages;
import com.zpedroo.rankuplevels.utils.config.Settings;
import com.zpedroo.rankuplevels.utils.cooldown.Cooldown;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static com.zpedroo.rankuplevels.utils.config.Settings.DEFAULT_LEVEL;

public class ClothesCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;
        Clothes clothes = DataManager.getInstance().getClothesByLevel(DEFAULT_LEVEL);
        if (clothes == null) return true;

        if (Cooldown.get().isInCooldown(player, this)) {
            player.sendMessage(StringUtils.replaceEach(Messages.COOLDOWN, new String[]{
                    "{cooldown}"
            }, new String[]{
                    String.valueOf(Cooldown.get().getTimeLeftInSeconds(player, this))
            }));
            return true;
        }

        Cooldown.get().addCooldown(player, this, Settings.CLOTHES_PICKUP_COOLDOWN);

        ItemStack[] clothesItems = clothes.getItems();
        if (!hasArmorEquipped(player)) {
            ArrayUtils.reverse(clothesItems); // fix armor order
            player.getInventory().setArmorContents(clothesItems);
            return true;
        }

        for (ItemStack item : clothesItems) {
            player.getInventory().addItem(item);
        }
        return false;
    }

    private boolean hasArmorEquipped(Player player) {
        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (item != null && !item.getType().equals(Material.AIR)) return true;
        }

        return false;
    }
}