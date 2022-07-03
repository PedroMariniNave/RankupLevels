package com.zpedroo.rankuplevels.utils.menu;

import com.zpedroo.rankuplevels.enums.FormulaType;
import com.zpedroo.rankuplevels.managers.DataManager;
import com.zpedroo.rankuplevels.objects.general.PlayerData;
import com.zpedroo.rankuplevels.utils.FileUtils;
import com.zpedroo.rankuplevels.utils.builder.InventoryBuilder;
import com.zpedroo.rankuplevels.utils.builder.InventoryUtils;
import com.zpedroo.rankuplevels.utils.builder.ItemBuilder;
import com.zpedroo.rankuplevels.utils.color.Colorize;
import com.zpedroo.rankuplevels.utils.formatter.NumberFormatter;
import com.zpedroo.rankuplevels.utils.formula.ExperienceManager;
import com.zpedroo.rankuplevels.utils.progress.ProgressConverter;
import org.apache.commons.lang.StringUtils;
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
                    NumberFormatter.getInstance().format(ExperienceManager.getFullLevelExperience(data.getLevel(), FormulaType.PLAYER_LEVEL)),
                    NumberFormatter.getInstance().formatThousand(data.getLevel()),
                    data.getReplacedLevelTag(),
                    ProgressConverter.convert(data.getExpAmount(), FormulaType.PLAYER_LEVEL),
                    NumberFormatter.getInstance().formatDecimal(ProgressConverter.getPercentage(data.getExpAmount(), FormulaType.PLAYER_LEVEL))
            }).build();
            int slot = FileUtils.get().getInt(file, "Inventory.items." + items + ".slot");

            inventory.addItem(item, slot, () -> {
                if (StringUtils.contains(action, ":")) {
                    String[] split = action.split(":");
                    String command = split.length > 1 ? split[1] : null;
                    if (command == null) return;

                    switch (split[0].toUpperCase()) {
                        case "PLAYER":
                            player.chat("/" + command);
                            break;
                        case "CONSOLE":
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), StringUtils.replaceEach(command, new String[]{
                                    "{player}"
                            }, new String[]{
                                    player.getName()
                            }));
                            break;
                    }
                }

                switch (action.toUpperCase()) {
                    case "CLOTHES":
                        openClothesMenu(player);
                        break;
                    case "TOP":
                        openTopMenu(player);
                        break;
                }
            }, InventoryUtils.ActionType.ALL_CLICKS);
        }

        inventory.open(player);
    }

    public void openClothesMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.CLOTHES;

        String title = Colorize.getColored(FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        InventoryBuilder inventory = new InventoryBuilder(title, size);

        String[] slots = FileUtils.get().getString(file, "Inventory.slots").replace(" ", "").split(",");
        int i = -1;

        for (String str : FileUtils.get().getSection(file, "Inventory.items")) {
            if (++i >= slots.length) i = 0;

            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Inventory.items." + str).build();
            int slot = Integer.parseInt(slots[i]);

            inventory.addItem(item, slot);
        }

        ItemStack backItem = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Back-Item").build();
        int slot = FileUtils.get().getInt(file, "Back-Item.slot");

        inventory.addItem(backItem, slot, () -> {
            openMainMenu(player);
        }, ActionType.ALL_CLICKS);

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
                    NumberFormatter.getInstance().format(ExperienceManager.getFullLevelExperience(data.getLevel(), FormulaType.PLAYER_LEVEL)),
                    NumberFormatter.getInstance().formatThousand(data.getLevel()),
                    data.getReplacedLevelTag(),
                    ProgressConverter.convert(data.getExpAmount(), FormulaType.PLAYER_LEVEL),
                    NumberFormatter.getInstance().formatDecimal(ProgressConverter.getPercentage(data.getExpAmount(), FormulaType.PLAYER_LEVEL)),
                    String.valueOf(pos)
            }).build();

            int slot = Integer.parseInt(slots[pos-1]);

            inventory.addItem(item, slot);
        }

        inventory.open(player);
    }
}