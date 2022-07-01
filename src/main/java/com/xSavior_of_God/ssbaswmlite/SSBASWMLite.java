package com.xSavior_of_God.ssbaswmlite;
/**
 * Developed by xSavior_of_God
 * Since 01-07-2022
 *
 * For SUPPORT
 *  <Telegram/>    @xSavior_of_God
 *  <Discord/>     https://discord.gg/5UuVdTE
 */

import com.xSavior_of_God.ssbaswmlite.listeners.SSBListeners;
import com.xSavior_of_God.ssbaswmlite.listeners.SaveLastLogoutFixListener;
import org.bukkit.plugin.java.JavaPlugin;

public class SSBASWMLite extends JavaPlugin {
    public static SSBASWMLite instance;
    public static SaveLastLogoutFix sllfix;
    private SSBSettings settings;

    public void onEnable() {
        SSBASWMLite.instance = this;
        settings = new SSBSettings();
        getServer().getPluginManager().registerEvents(new SSBListeners(), this);
        if (getSettings().SaveLastLogout) {
            sllfix = new SaveLastLogoutFix();
            getServer().getPluginManager().registerEvents(new SaveLastLogoutFixListener(), this);
        }
    }

    public static SSBSettings getSettings() {
        return SSBASWMLite.instance.settings;
    }

    public void onDisable() {
        if (getSettings().SaveLastLogout) {
            sllfix.disable();
        }
        settings = null;
        SSBASWMLite.sllfix = null;
        SSBASWMLite.instance = null;
    }
}
