package com.xSavior_of_God.ssbaswmlite.utils;
/**
 * Developed by xSavior_of_God
 * Since 01-07-2022
 *
 * For SUPPORT
 *  <Telegram/>    @xSavior_of_God
 *  <Discord/>     https://discord.gg/5UuVdTE
 */

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LazyWorldLocation extends Location {
    private final String worldName;

    public LazyWorldLocation(String worldName, double x, double y, double z, float pitch, float yaw) {
        super(Bukkit.getWorld(worldName), x, y, z, pitch, yaw);
        this.worldName = worldName;
    }

    @Override
    public World getWorld() {
        if (worldName != null)
            setWorld(Bukkit.getWorld(worldName));

        return super.getWorld();
    }

    public String getWorldName() {
        return worldName;
    }

}
