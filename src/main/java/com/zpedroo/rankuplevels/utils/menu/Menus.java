package com.zpedroo.rankuplevels.utils.menu;

import com.zpedroo.rankuplevels.managers.DataManager;
import com.zpedroo.rankuplevels.objects.PlayerData;
import com.zpedroo.rankuplevels.utils.FileUtils;
import com.zpedroo.rankuplevels.utils.builder.InventoryBuilder;
import com.zpedroo.rankuplevels.utils.builder.InventoryUtils;
import com.zpedroo.rankuplevels.utils.builder.ItemBuilder;
import com.zpedroo.rankuplevels.utils.color.Colorize;
import com.zpedroo.rankuplevels.utils.formatter.NumberFormatter;
import com.zpedroo.rankuplevels.utils.formula.ExperienceManager;
import com.zpedroo.rankuplevels.utils.progress.ProgressConverter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Menus extends InventoryUtils {

    private static Menus instance;
    public static Menus getInstance() { return instance; }

    public Menus() {
        instance = this;
    }

    public void openMainMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.MAIN;

        String title = Colorize.getColored(FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        InventoryBuilder inventory = new InventoryBuilder(title, size);
        PlayerData data = DataManager.getInstance().getPlayerData(player);

        for (String items : FileUtils.get().getSection(file, "Inventory.items")) {
            String action = FileUtils.get().getString(file, "Inventory.items." + items + ".action");
            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Inventory.items." + items, new String[]{
                    "{player}",
                    "{xp}",
                    "{next_xp}",
                    "{level}",
                    "{level_tag}",
                    "{progress_bar}",
                    "{progress_percentage}"
            }, new String[]{
                    player.getName(),
                    NumberFormatter.getInstance().format(data.getExpAmount()),
                    NumberFormatter.getInstance().format(ExperienceManager.getFullLevelExperience(data.getLevel())),
                    NumberFormatter.getInstance().formatThousand(data.getLevel()),
                    data.getReplacedLevelTag(),
                    ProgressConverter.convert(data.getExpAmount()),
                    NumberFormatter.getInstance().formatDecimal(ProgressConverter.getPercentage(data.getExpAmount()))
            }).build();
            int slot = FileUtils.get().getInt(file, "Inventory.items." + items + ".slot");

            inventory.addItem(item, slot, () -> {
                switch (action.toUpperCase()) {
                    case "TOP":
                        openTopMenu(player);
                        break;
                }
            }, InventoryUtils.ActionType.ALL_CLICKS);
        }

        inventory.open(player);
    }

    public void openTopMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.TOP;

        String title = Colorize.getColored(FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        InventoryBuilder inventory = new InventoryBuilder(title, size);

        int pos = 0;
        String[] slots = FileUtils.get().getString(file, "Inventory.slots").replace(" ", "").split(",");

        for (PlayerData data : DataManager.getInstance().getCache().getTopRanks()) {
            if (++pos > slots.length) break;

            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Item", new String[]{
                    "{player}",
                    "{xp}",
                    "{next_xp}",
                    "{level}",
                    "{level_tag}",
                    "{progress_bar}",
                    "{progress_percentage}",
                    "{pos}"
            }, new String[]{
                    Bukkit.getOfflinePlayer(data.getUniqueId()).getName(),
                    NumberFormatter.getInstance().format(data.getExpAmount()),
                    NumberFormatter.getInstance().format(ExperienceManager.getFullLevelExperience(data.getLevel())),
                    NumberFormatter.getInstance().formatThousand(data.getLevel()),
                    data.getReplacedLevelTag(),
                    ProgressConverter.convert(data.getExpAmount()),
                    NumberFormatter.getInstance().formatDecimal(ProgressConverter.getPercentage(data.getExpAmount())),
                    String.valueOf(pos)
            }).build();

            int slot = Integer.parseInt(slots[pos-1]);

            inventory.addItem(item, slot);
        }

        inventory.open(player);
    }
}