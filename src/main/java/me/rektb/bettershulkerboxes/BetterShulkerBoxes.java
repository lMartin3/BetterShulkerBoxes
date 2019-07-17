package me.rektb.bettershulkerboxes;

import me.rektb.bettershulkerboxes.events.DupePreventEvents;
import me.rektb.bettershulkerboxes.events.InteractEvent;
import me.rektb.bettershulkerboxes.events.InvCloseEvent;
import me.rektb.bettershulkerboxes.events.PlyrJoinEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterShulkerBoxes
        extends JavaPlugin
        implements Listener {
    public boolean updatefound = false;
    public String lastver = "";
    public String resourceurl = "";
    private UpdateChecker updater = new UpdateChecker(this, 58837);

    public void onEnable() {
        loadConfig();
        getServer().getPluginManager().registerEvents(new InteractEvent(), this);
        getServer().getPluginManager().registerEvents(new InvCloseEvent(), this);
        getServer().getPluginManager().registerEvents(new DupePreventEvents(), this);
        getServer().getPluginManager().registerEvents(new PlyrJoinEvent(), this);

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Better Shulkerboxes enabled - Plugin written by Rektb");
        Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {
            public void run() {
                BetterShulkerBoxes.this.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "BSB is checking for updates...");
                try {
                    if (BetterShulkerBoxes.this.updater.checkForUpdates()) {
                        BetterShulkerBoxes.this.getLogger().info(ChatColor.GREEN + "Update found! New version: " + BetterShulkerBoxes.this.updater.getLatestVersion() + " download: " + BetterShulkerBoxes.this.updater.getResourceURL());
                        BetterShulkerBoxes.this.updatefound = true;
                        BetterShulkerBoxes.this.lastver = BetterShulkerBoxes.this.updater.getLatestVersion();
                        BetterShulkerBoxes.this.resourceurl = BetterShulkerBoxes.this.updater.getResourceURL();
                    }
                } catch (Exception e) {
                    BetterShulkerBoxes.this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Error! BSB could not check for updates:");
                    e.printStackTrace();
                }
            }
        });
    }

    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "Better Shulkerboxes disabled");
    }

    private void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
}
