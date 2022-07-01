package com.xSavior_of_God.ssbaswmlite;
/**
 * Developed by xSavior_of_God
 * Since 01-07-2022
 *
 * For SUPPORT
 *  <Telegram/>    @xSavior_of_God
 *  <Discord/>     https://discord.gg/5UuVdTE
 */

import com.xSavior_of_God.ssbaswmlite.utils.CommentedConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class SSBSettings {
    protected static FileConfiguration config;
    public String DataSource, Difficulty, DifficultyNether, DifficultyEnd, WorldNameNormal, WorldNameNether, WorldNameEnd;
    public Boolean SaveLastLogout;

    public SSBSettings() {
        SSBASWMLite.instance.reloadConfig();
        File configFile = new File(SSBASWMLite.instance.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            SSBASWMLite.instance.saveResource("config.yml", false);
        }
        CommentedConfiguration cfg = CommentedConfiguration.loadConfiguration(configFile);
        try {
            cfg.syncWithConfig(configFile, SSBASWMLite.instance.getResource("config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        config = SSBASWMLite.instance.getConfig();
        this.reloadConfig();
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Configuration Loaded!");
    }

    public void reloadConfig() {
        this.DataSource = config.getString("main.data-source", "file");

        this.WorldNameNormal = config.getString("main.world.normal.name", "SuperiorIslandWorld");
        this.Difficulty = config.getString("main.world.normal.difficulty", "NORMAL");

        this.WorldNameNether = config.getString("main.world.nether.name", "SuperiorIslandWorld_nether");
        this.DifficultyNether = config.getString("main.world.nether.difficulty", "NORMAL");

        this.WorldNameEnd = config.getString("main.world.the_end.name", "SuperiorIslandWorld_the_end");
        this.DifficultyEnd = config.getString("main.world.the_end.difficulty", "NORMAL");

        this.SaveLastLogout = config.getBoolean("main.save-last-logout-position", true);
    }

}
