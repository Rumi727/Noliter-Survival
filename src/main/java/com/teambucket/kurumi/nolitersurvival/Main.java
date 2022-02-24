package com.teambucket.kurumi.nolitersurvival;

import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Logger;

public final class Main extends JavaPlugin
{
    public static Server server;
    public static Logger debug;

    @Override
    public void onEnable()
    {
        server = getServer();
        debug = getLogger();


        List<World> worlds = Bukkit.getWorlds();
        for (World world : worlds)
        {
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, true);
            world.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, false);
            world.setGameRule(GameRule.LOG_ADMIN_COMMANDS, false);
            world.setGameRule(GameRule.REDUCED_DEBUG_INFO, true);
        }

        World world = worlds.get(0);

        WorldBorder worldBorder = world.getWorldBorder();
        worldBorder.setCenter(new Location(world, 0, 0, 0));
        worldBorder.setSize(16384.0);
        world.setSpawnLocation(world.getHighestBlockAt(0, 0).getLocation());

        getServer().getPluginManager().registerEvents(new EventListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
