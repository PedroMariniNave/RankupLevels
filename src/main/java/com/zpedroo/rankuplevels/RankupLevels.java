package com.zpedroo.rankuplevels;

import com.zpedroo.rankuplevels.commands.RankupLevelsCmd;
import com.zpedroo.rankuplevels.hooks.PlaceholderAPIHook;
import com.zpedroo.rankuplevels.listeners.PlayerGeneralListeners;
import com.zpedroo.rankuplevels.listeners.TagListeners;
import com.zpedroo.rankuplevels.managers.DataManager;
import com.zpedroo.rankuplevels.mysql.DBConnection;
import com.zpedroo.rankuplevels.tasks.SaveTask;
import com.zpedroo.rankuplevels.utils.FileUtils;
import com.zpedroo.rankuplevels.utils.formatter.NumberFormatter;
import com.zpedroo.rankuplevels.utils.menu.Menus;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;

import static com.zpedroo.rankuplevels.utils.config.Settings.ALIASES;
import static com.zpedroo.rankuplevels.utils.config.Settings.COMMAND;

public class RankupLevels extends JavaPlugin {

    private static RankupLevels instance;
    public static RankupLevels get() { return instance; }

    public void onEnable() {
        instance = this;
        new FileUtils(this);

        if (!isMySQLEnabled(getConfig())) {
            getLogger().log(Level.SEVERE, "MySQL are disabled! You need to enable it.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        new DBConnection(getConfig());
        new NumberFormatter(getConfig());
        new DataManager();
        new Menus();
        new SaveTask(this);

        registerHooks();
        registerListeners();
        registerCommand(COMMAND, ALIASES, new RankupLevelsCmd());
    }

    public void onDisable() {
        if (!isMySQLEnabled(getConfig())) return;

        try {
            DataManager.getInstance().saveAllPlayersData();
            DBConnection.getInstance().closeConnection();
        } catch (Exception ex) {
            getLogger().log(Level.SEVERE, "An error occurred while trying to save data!");
            ex.printStackTrace();
        }
    }

    private void registerCommand(String command, List<String> aliases, CommandExecutor executor) {
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);

            PluginCommand pluginCmd = constructor.newInstance(command, this);
            pluginCmd.setAliases(aliases);
            pluginCmd.setExecutor(executor);

            Field field = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) field.get(Bukkit.getPluginManager());
            commandMap.register(getName().toLowerCase(), pluginCmd);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerGeneralListeners(), this);
    }

    private void registerHooks() {
        if (getServer().getPluginManager().getPlugin("Legendchat") != null) {
            getServer().getPluginManager().registerEvents(new TagListeners(), this);
        }

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPIHook(this).register();
        }
    }

    private boolean isMySQLEnabled(FileConfiguration file) {
        return file.getBoolean("MySQL.enabled", false);
    }
}