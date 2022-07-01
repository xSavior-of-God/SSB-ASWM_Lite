package com.xSavior_of_God.ssbaswmlite.listeners;
/**
 * Developed by xSavior_of_God
 * Since 01-07-2022
 *
 * For SUPPORT
 *  <Telegram/>    @xSavior_of_God
 *  <Discord/>     https://discord.gg/5UuVdTE
 */

import com.xSavior_of_God.ssbaswmlite.SSBASWMLite;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SaveLastLogoutFixListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        SSBASWMLite.sllfix.tryTeleport(e.getPlayer());
    }

    @EventHandler
    public void onPlayerLogout(PlayerQuitEvent e) {
        SSBASWMLite.sllfix.updateLogoutLocation(e.getPlayer());
    }

    @EventHandler
    public void onPlayerLogout(PlayerKickEvent e) {
        SSBASWMLite.sllfix.updateLogoutLocation(e.getPlayer());
    }

}
