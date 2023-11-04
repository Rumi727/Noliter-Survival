package kr.kro.teambucket.kurumi.plugin.nolitersurvival;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.inventory.meta.BookMeta;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class EventListener implements Listener
{
    @EventHandler
    void OnPlayerLogin(PlayerLoginEvent e)
    {
        if (e.getResult() == PlayerLoginEvent.Result.KICK_FULL)
            e.allow();
    }

    @EventHandler
    void OnPlayerJoin(PlayerJoinEvent e)
    {
        e.joinMessage(null);

        Player player = e.getPlayer();

        if (!player.hasPlayedBefore())
        {
            player.teleport(getSpawnLocation(player.getName()));
        }

        player.setCompassTarget(Bukkit.getWorlds().get(0).getSpawnLocation());
    }

    @EventHandler
    void OnPlayerQuit(PlayerQuitEvent e)
    {
        e.quitMessage(null);
    }

    @EventHandler(ignoreCancelled = true)
    void OnTabComplete(TabCompleteEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler
    void OnPlayerCommandPreprocess(PlayerCommandPreprocessEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    void OnPlayerDeath(PlayerDeathEvent e)
    {
        e.deathMessage(Component.text("사람이 죽었다.").color(TextColor.color(255, 85, 85)));
    }

    @SuppressWarnings("deprecation")
    @EventHandler(ignoreCancelled = true)
    void OnAsyncPlayerChat(AsyncPlayerChatEvent e) { e.setCancelled(true); }

    @EventHandler
    void OnPaperServerListPing(PaperServerListPingEvent e)
    {
        Calendar c = Calendar.getInstance();

        e.setNumPlayers(c.get(Calendar.YEAR) * 10000 + (c.get(Calendar.MONTH) + 1) * 100 + c.get(Calendar.DAY_OF_MONTH));
        e.setMaxPlayers(c.get(Calendar.HOUR) * 10000 + c.get(Calendar.MINUTE) * 100 + c.get(Calendar.SECOND));
        e.motd(Component.text("AIMLESS SERVER " + c.get(Calendar.YEAR)).color(TextColor.color(255, 255, 255)));
        e.getPlayerSample().clear();
    }

    @EventHandler
    void OnPlayerSign(SignChangeEvent e)
    {
        Block block = e.getBlock();
        int x = block.getX();
        int z = block.getZ();

        if (x < -16 || x > 15 || z < -16 || z > 15)
        {
            List<Component> list = e.lines();
            int length = list.size();
            for (int i = 0; i < length; i++)
                e.line(i, Component.text(""));
        }
    }

    @EventHandler(ignoreCancelled = true)
    void OnPlayerBookEdit(PlayerEditBookEvent e)
    {
        Location location = e.getPlayer().getLocation();
        double x = location.getX();
        double z = location.getZ();

        if (x < -16 || x > 15 || z < -16 || z > 15)
        {
            e.setCancelled(true);

            BookMeta meta = e.getNewBookMeta();

            meta.title(Component.text(""));

            int pages = meta.getPageCount();
            for (int i = 1; i <= pages; i++)
            {
                meta.page(i, Component.text(""));
            }
        }
    }

    @EventHandler
    void OnPlayerRespawn(PlayerRespawnEvent e)
    {
        if (e.isBedSpawn() || e.isAnchorSpawn())
            return;

        e.setRespawnLocation(getSpawnLocation(e.getPlayer().name().toString()));
    }

    Location getSpawnLocation(String name)
    {
        int seed = name.hashCode();
        Random random = new Random();
        random.setSeed(seed);
        World world = Bukkit.getWorlds().get(0);
        WorldBorder border = world.getWorldBorder();
        double size = border.getSize() / 2.0;

        double x = random.nextDouble() * size - size / 2.0;
        double z = random.nextDouble() * size - size / 2.0;
        Block block = world.getHighestBlockAt((int)Math.floor(x), (int)Math.floor(z));

        return block.getLocation().add(0.5, 1.0, 0.5);
    }

    @EventHandler
    void OnPlayerKick(PlayerKickEvent e)
    {
        e.setCancelled(true);
    }
}
