package me.rektb.bettershulkerboxes.events;

import me.rektb.bettershulkerboxes.BetterShulkerBoxes;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlyrJoinEvent
        implements Listener {
    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        BetterShulkerBoxes plugin = BetterShulkerBoxes.getPlugin(BetterShulkerBoxes.class);
        String cfg_prefix = plugin.getConfig().getString("prefix");
        String prefix = ChatColor.translateAlternateColorCodes('&', cfg_prefix);
        if ((e.getPlayer().hasPermission("bettershulkerboxes.updatenotify")) && (plugin.updatefound)) {
            e.getPlayer().sendMessage(prefix + ChatColor.AQUA + "Version " + ChatColor.YELLOW + (plugin).lastver + ChatColor.AQUA + " is avaible! Currentli Download at " + ChatColor.YELLOW + (plugin).resourceurl);
        }
    }
}
