package me.rektb.bettershulkerboxes.events;

import me.rektb.bettershulkerboxes.BetterShulkerBoxes;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlyrJoinEvent implements Listener {


    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        BetterShulkerBoxes plugin = BetterShulkerBoxes.getPlugin(BetterShulkerBoxes.class);
        String cfg_prefix = plugin.getConfig().getString("prefix");
        String prefix = ChatColor.translateAlternateColorCodes('&', cfg_prefix);
        Player p = e.getPlayer();

        //Check permissions and everything, then send a message
        if ((p.hasPermission("bettershulkerboxes.updatenotify")) && (plugin.updatefound)) {
            p.sendMessage(prefix + ChatColor.AQUA + "Version " + ChatColor.YELLOW +
                    (plugin).lastver + ChatColor.AQUA + " is available! Currently using " + ChatColor.YELLOW +
                    plugin.getDescription().getVersion() + ChatColor.AQUA +
                    ". Download the new version at " + ChatColor.YELLOW + (plugin).resourceurl);
        }
    }
}
