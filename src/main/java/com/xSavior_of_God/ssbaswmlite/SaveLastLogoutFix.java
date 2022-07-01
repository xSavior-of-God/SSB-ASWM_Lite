package com.xSavior_of_God.ssbaswmlite;
/**
 * Developed by xSavior_of_God
 * Since 01-07-2022
 *
 * For SUPPORT
 *  <Telegram/>    @xSavior_of_God
 *  <Discord/>     https://discord.gg/5UuVdTE
 */

import com.xSavior_of_God.ssbaswmlite.databases.Database;
import com.xSavior_of_God.ssbaswmlite.databases.sql.SQLite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class SaveLastLogoutFix {
    private Database data = new SQLite();
    private Map<String, Location> locations = new HashMap<>();

    public SaveLastLogoutFix() {
        data.connect("", "", "", "");
        if (data.checkConnection()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aDatabase Connected!"));
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDatabase not connected, check for errors!!"));
            SSBASWMLite.instance.onDisable();
            return;
        }

        try {
            loadData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadData() throws SQLException {
        locations = new HashMap<>();
        Statement stmt = data.getConnection().createStatement();
        final ResultSet result = data.query("SELECT * FROM lastLogoutLocations;", stmt);
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Start Loading Last Logout Locations..."));
        long time = System.currentTimeMillis();
        while (result.next()) {
            World world = Bukkit.getWorld(result.getString("world"));
            if (world == null) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThe world '" + result.getString("world") + "' does not exist or has not yet been loaded!"));
                continue;
            }
            locations.put(result.getString("UUID"), new Location(world, result.getInt("x"), result.getInt("y"), result.getInt("z"), result.getFloat("yaw"), result.getFloat("pitch")));
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aLoaded " + locations.size() + " Last Logout Locations with success &7(in about " + (System.currentTimeMillis() - time) + "ms)"));
    }

    public void tryTeleport(Player p) {
        Location loc = locations.get(p.getUniqueId().toString());
        if (loc != null) {
            if (p.getLocation() != loc) {
                p.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
            }
        }
    }

    public void updateLogoutLocation(Player p) {
        Location loc = p.getLocation();
        String query = "";
        if (locations.get(p.getUniqueId().toString()) == null) {
            //create
            query = "INSERT INTO lastLogoutLocations (Username, UUID, world, x, y, z, yaw, pitch) VALUES ('" + p.getName() + "', '" + p.getUniqueId().toString() + "', '" + loc.getWorld().getName() + "','" + (int) loc.getX() + "','" + (int) loc.getY() + "','" + (int) loc.getZ() + "', '" + loc.getYaw() + "','" + loc.getPitch() + "') ;";

        } else {
            //update
            query = "UPDATE lastLogoutLocations SET world = '" + loc.getWorld().getName() + "', x = '" + (int) loc.getX() + "', y = '" + (int) loc.getY() + "', z = '" + (int) loc.getZ() + "', yaw = '" + loc.getYaw() + "', pitch = '" + loc.getPitch() + "' WHERE UUID = '" + p.getUniqueId().toString() + "';";

        }
        data.update(query);
    }

    public void disable() {
        locations = null;
        data.disconnect();
    }
}
