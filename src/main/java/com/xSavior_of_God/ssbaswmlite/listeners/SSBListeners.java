package com.xSavior_of_God.ssbaswmlite.listeners;
/**
 * Developed by xSavior_of_God
 * Since 01-07-2022
 *
 * For SUPPORT
 *  <Telegram/>    @xSavior_of_God
 *  <Discord/>     https://discord.gg/5UuVdTE
 */

import com.bgsoftware.superiorskyblock.api.events.PluginInitializeEvent;
import com.xSavior_of_God.ssbaswmlite.SSBWorldManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SSBListeners implements Listener {

    @EventHandler
    public void onSSBInit(PluginInitializeEvent e) {
        e.getPlugin().getProviders().setWorldsProvider(SSBWorldManager.createManager());
    }

}
