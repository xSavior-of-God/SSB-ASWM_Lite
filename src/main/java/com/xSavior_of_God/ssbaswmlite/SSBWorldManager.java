package com.xSavior_of_God.ssbaswmlite;
/**
 * Developed by xSavior_of_God
 * Since 01-07-2022
 *
 * For SUPPORT
 *  <Telegram/>    @xSavior_of_God
 *  <Discord/>     https://discord.gg/5UuVdTE
 */

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.hooks.WorldsProvider;
import com.bgsoftware.superiorskyblock.api.island.Island;

import com.bgsoftware.superiorskyblock.api.wrappers.BlockPosition;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import com.grinderwolf.swm.plugin.config.ConfigManager;
import com.grinderwolf.swm.plugin.config.WorldData;
import com.grinderwolf.swm.plugin.config.WorldsConfig;
import com.xSavior_of_God.ssbaswmlite.utils.SBlockPosition;
import org.bukkit.*;
import org.bukkit.block.BlockFace;

import java.io.IOException;
import java.util.*;

public class SSBWorldManager {

    public static WorldsProvider createManager() {
        return new SlimeWorldsProvider();
    }

    private static class SlimeWorldsProvider implements WorldsProvider {
        private final Set<BlockPosition> servedPositions = Sets.newHashSet();
        private final EnumMap<World.Environment, World> islandWorlds = new EnumMap<>(World.Environment.class);
        private final SSBASWMLite plugin = SSBASWMLite.instance;
        public static final SlimePlugin slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        ;
        private WorldData defaultWorldData = buildDefaultWorldData();
        private WorldData defaultNetherWorldData = buildDefaultNetherWorldData();
        private WorldData defaultEndWorldData = buildDefaultEndWorldData();

        private WorldData buildDefaultWorldData() {
            WorldData worldData = new WorldData();
            worldData.setDataSource(plugin.getSettings().DataSource);
            worldData.setEnvironment("NORMAL");
            worldData.setDifficulty(plugin.getSettings().Difficulty);
            worldData.setLoadOnStartup(true);
            return worldData;
        }

        private WorldData buildDefaultNetherWorldData() {
            WorldData worldData = new WorldData();
            worldData.setDataSource(plugin.getSettings().DataSource);
            worldData.setEnvironment("NETHER");
            worldData.setDefaultBiome("minecraft:nether_wastes");
            worldData.setDifficulty(plugin.getSettings().DifficultyNether);
            worldData.setLoadOnStartup(true);
            return worldData;
        }

        private WorldData buildDefaultEndWorldData() {
            WorldData worldData = new WorldData();

            worldData.setDataSource(plugin.getSettings().DataSource);
            worldData.setEnvironment("THE_END");
            worldData.setDefaultBiome("minecraft:the_end");
            worldData.setDifficulty(plugin.getSettings().DifficultyEnd);
            worldData.setLoadOnStartup(true);
            return worldData;
        }

        @Override
        public void prepareWorlds() {
            Difficulty difficulty = Difficulty.valueOf(SuperiorSkyblockAPI.getSettings().getWorlds().getDifficulty());
            if (SuperiorSkyblockAPI.getSettings().getWorlds().getNormal().isEnabled())
                loadWorld(plugin.getSettings().WorldNameNormal, difficulty, World.Environment.NORMAL);
            if (SuperiorSkyblockAPI.getSettings().getWorlds().getNether().isEnabled())
                loadWorld(plugin.getSettings().WorldNameNether, difficulty, World.Environment.NETHER);
            if (SuperiorSkyblockAPI.getSettings().getWorlds().getEnd().isEnabled()) {
                World endWorld = loadWorld(plugin.getSettings().WorldNameEnd, difficulty, World.Environment.THE_END);
                if (SuperiorSkyblockAPI.getSettings().getWorlds().getEnd().isDragonFight()) {
                    //TODO
                    // SuperiorSkyblockAPI -> getNMSDragonFight().prepareEndWorld(endWorld);
                    // https://github.com/BG-Software-LLC/SuperiorSkyblock2/blob/7664dde301e1d5821963ddee60aa8debc3effda4/src/main/java/com/bgsoftware/superiorskyblock/external/worlds/WorldsProvider_Default.java#L42
                }
            }
        }

        @Override
        public World getIslandsWorld(Island island, World.Environment environment) {
            Preconditions.checkNotNull(environment, "environment parameter cannot be null.");
            return islandWorlds.get(environment);
        }

        @Override
        public boolean isIslandsWorld(World world) {
            Preconditions.checkNotNull(world, "world parameter cannot be null.");
            World islandsWorld = getIslandsWorld(null, world.getEnvironment());
            return islandsWorld != null && world.getUID().equals(islandsWorld.getUID());
        }

        @Override
        public Location getNextLocation(Location previousLocation, int islandsHeight, int maxIslandSize, UUID islandOwner, UUID islandUUID) {
            Preconditions.checkNotNull(previousLocation, "previousLocation parameter cannot be null.");

            Location location = previousLocation.clone();
            location.setY(islandsHeight);
            BlockFace islandFace = getIslandFace(location);
            int islandRange = maxIslandSize * 3;

            if (islandFace == BlockFace.NORTH) {
                location.add(islandRange, 0, 0);
            } else if (islandFace == BlockFace.EAST) {
                if (location.getX() == -location.getZ())
                    location.add(islandRange, 0, 0);
                else if (location.getX() == location.getZ())
                    location.subtract(islandRange, 0, 0);
                else
                    location.add(0, 0, islandRange);
            } else if (islandFace == BlockFace.SOUTH) {
                if (location.getX() == -location.getZ())
                    location.subtract(0, 0, islandRange);
                else
                    location.subtract(islandRange, 0, 0);
            } else if (islandFace == BlockFace.WEST) {
                if (location.getX() == location.getZ())
                    location.add(islandRange, 0, 0);
                else
                    location.subtract(0, 0, islandRange);
            }
            BlockPosition blockPosition = new SBlockPosition(location);

            if (servedPositions.contains(blockPosition) || SuperiorSkyblockAPI.getGrid().getIslandAt(location) != null) {
                return getNextLocation(location.clone(), islandsHeight, maxIslandSize, islandOwner, islandUUID);
            }

            servedPositions.add(blockPosition);

            return location;
        }

        @Override
        public void finishIslandCreation(Location islandLocation, UUID islandOwner, UUID islandUUID) {
            Preconditions.checkNotNull(islandLocation, "islandLocation parameter cannot be null.");
            servedPositions.remove(new SBlockPosition(islandLocation));
        }

        @Override
        public void prepareTeleport(Island island, Location location, Runnable finishCallback) {
            finishCallback.run();
        }

        @Override
        public boolean isNormalEnabled() {
            return SuperiorSkyblockAPI.getSettings().getWorlds().getNormal().isEnabled();
        }

        @Override
        public boolean isNormalUnlocked() {
            return SuperiorSkyblockAPI.getSettings().getWorlds().getNormal().isUnlocked();
        }

        @Override
        public boolean isNetherEnabled() {
            return SuperiorSkyblockAPI.getSettings().getWorlds().getNether().isEnabled();
        }

        @Override
        public boolean isNetherUnlocked() {
            return SuperiorSkyblockAPI.getSettings().getWorlds().getNether().isUnlocked();
        }

        @Override
        public boolean isEndEnabled() {
            return SuperiorSkyblockAPI.getSettings().getWorlds().getEnd().isEnabled();
        }

        @Override
        public boolean isEndUnlocked() {
            return SuperiorSkyblockAPI.getSettings().getWorlds().getEnd().isUnlocked();
        }


        private BlockFace getIslandFace(Location location) {
            //Possibilities: North / East
            if (location.getX() >= location.getZ()) {
                return -location.getX() > location.getZ() ? BlockFace.NORTH : BlockFace.EAST;
            }
            //Possibilities: South / West
            else {
                return -location.getX() > location.getZ() ? BlockFace.WEST : BlockFace.SOUTH;
            }
        }

        private World loadWorld(String worldName, Difficulty difficulty, World.Environment environment) {
            // World Difficulty is ignored as WorldData is preloaded to optimize world creation times,
            // please specify the difficulty directly in the config.yml
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                world = Bukkit.getWorld(loadAndGetWorld(worldName, environment).getName());
            }
            islandWorlds.put(environment, world);

            return world;
        }

        public SlimeWorld loadAndGetWorld(String worldName, World.Environment environment) {
            if (environment.equals(World.Environment.CUSTOM)) {
                return null;
            }

            WorldData worldData = ConfigManager.getWorldConfig().getWorlds().get(worldName);
            SlimeWorld slimeWorld = null;
            try {
                // No world was found, creating a new world.
                if (worldData == null) {
                    if (worldName.toLowerCase().contains("nether")) {
                        SlimePropertyMap slimePropertyMap = defaultNetherWorldData.toPropertyMap();
                        slimePropertyMap.setString(SlimeProperties.ENVIRONMENT, environment.name().toUpperCase());
                        slimeWorld = slimePlugin.createEmptyWorld(slimePlugin.getLoader(defaultNetherWorldData.getDataSource()),
                            worldName, defaultNetherWorldData.isReadOnly(), slimePropertyMap);

                        // Saving the world
                        WorldsConfig config = ConfigManager.getWorldConfig();
                        config.getWorlds().put(worldName, defaultNetherWorldData);
                        config.save();
                    } else if (worldName.toLowerCase().contains("the_end")) {
                        SlimePropertyMap slimePropertyMap = defaultEndWorldData.toPropertyMap();
                        slimePropertyMap.setString(SlimeProperties.ENVIRONMENT, environment.name().toUpperCase());
                        slimeWorld = slimePlugin.createEmptyWorld(slimePlugin.getLoader(defaultEndWorldData.getDataSource()),
                            worldName, defaultEndWorldData.isReadOnly(), slimePropertyMap);

                        // Saving the world
                        WorldsConfig config = ConfigManager.getWorldConfig();
                        config.getWorlds().put(worldName, defaultEndWorldData);
                        config.save();
                    } else {
                        SlimePropertyMap slimePropertyMap = defaultWorldData.toPropertyMap();
                        slimePropertyMap.setString(SlimeProperties.ENVIRONMENT, environment.name().toUpperCase());
                        slimeWorld = slimePlugin.createEmptyWorld(slimePlugin.getLoader(defaultWorldData.getDataSource()),
                            worldName, defaultWorldData.isReadOnly(), slimePropertyMap);

                        // Saving the world
                        WorldsConfig config = ConfigManager.getWorldConfig();
                        config.getWorlds().put(worldName, defaultWorldData);
                        config.save();
                    }
                } else {
                    SlimeLoader loader = slimePlugin.getLoader(worldData.getDataSource());
                    try {
                        if (loader.isWorldLocked(worldName)) {
                            loader.unlockWorld(worldName);
                        }
                    } catch (UnknownWorldException | IOException e) {
                        e.printStackTrace();
                    }
                    slimeWorld = slimePlugin.loadWorld(loader, worldName, false, worldData.toPropertyMap());
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (Bukkit.getWorld(worldName) == null)
                if (slimeWorld != null)
                    slimePlugin.generateWorld(slimeWorld);
                else
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "World " + worldName + " is NULL!");

            if (slimeWorld.isReadOnly()) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "World " + worldName + " is Read ONLY!");
            }

            if (Bukkit.getWorld(worldName) == null)
                slimePlugin.generateWorld(slimeWorld);

            return slimeWorld;
        }

    }
}