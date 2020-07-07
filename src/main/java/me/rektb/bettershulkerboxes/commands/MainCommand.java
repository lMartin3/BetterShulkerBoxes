package me.rektb.bettershulkerboxes.commands;

import me.rektb.bettershulkerboxes.BetterShulkerBoxes;
import me.rektb.bettershulkerboxes.utils.ConfigurationImport;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {
    private BetterShulkerBoxes plugin = BetterShulkerBoxes.getPlugin(BetterShulkerBoxes.class);
    private ConfigurationImport cfgi = plugin.cfgi;
    public String command = "bsb";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase(command)) {
            if (args.length == 0) {
                pluginInfo(sender);
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("bettershulkerboxes.reload")) {
                    sender.sendMessage(cfgi.prefix + cfgi.nopermsmsg);
                    return false;
                }
                plugin.reloadConfig();
                plugin.loadConfig();
                if (!plugin.cfgi.checkConfigurationValidity().isEmpty()) {
                    sender.sendMessage(cfgi.prefix + cfgi.configreloaderror);
                    plugin.checkConfigValidity();
                    return false;
                }
                this.plugin = BetterShulkerBoxes.getPlugin(BetterShulkerBoxes.class);
                this.cfgi = plugin.cfgi;
                plugin.checkConfigValidity();
                plugin.newInstances();
                sender.sendMessage(cfgi.prefix + cfgi.configreload);
            } else if (args[0].equalsIgnoreCase("check")) {
                if (!sender.hasPermission("bettershulkerboxes.updatenotify")) {
                    sender.sendMessage(cfgi.prefix + cfgi.nopermsmsg);
                    return false;
                }
                // This does not run on a separate thread but it is meant to be used by admins only.
                if (plugin.updater.checkForUpdates()) {
                    sender.sendMessage(cfgi.prefix + ChatColor.AQUA + "Version " + ChatColor.YELLOW +
                            (plugin).lastver + ChatColor.AQUA + " is available! Currently using " + ChatColor.YELLOW +
                            plugin.getDescription().getVersion() + ChatColor.AQUA +
                            ". Download the new version at " + ChatColor.YELLOW + (plugin).resourceurl);
                    sender.sendMessage(cfgi.prefix + ChatColor.AQUA + "Version changes: ");
                    for (String s : plugin.updater.getChangelog()) {
                        sender.sendMessage(ChatColor.DARK_AQUA + "> " + ChatColor.GRAY + s);
                    }
                } else {
                    sender.sendMessage(cfgi.prefix + ChatColor.GREEN + "You're using the latest version of " +
                            "BetterShulkerBoxes (" + ChatColor.YELLOW + plugin.getDescription().getVersion() + ChatColor.GREEN + ")");
                }

            } else {
                pluginInfo(sender);
            }
        }
        return false;
    }

    // This is sent when there are no arguments or an invalid one
    private void pluginInfo(CommandSender s) {
        s.sendMessage(cfgi.prefix + ChatColor.AQUA + "This server is running " + ChatColor.YELLOW + "Better Shulker Boxes v" + plugin.getDescription().getVersion() + ChatColor.AQUA + ".");
        if (s.hasPermission("bettershulkerboxes.reload")) {
            s.sendMessage(cfgi.prefix + ChatColor.AQUA + "Use " + ChatColor.YELLOW + "/bsb reload" + ChatColor.AQUA + " to reload the configuration.");
        }
        if (s.hasPermission("bettershulkerboxes.updatenotify")) {
            s.sendMessage(cfgi.prefix + ChatColor.AQUA + "Use " + ChatColor.YELLOW + "/bsb check" + ChatColor.AQUA + " to check for updates.");
        }
    }

    // Does the same as in other classes
    public void getNewInstances() {
        this.plugin = BetterShulkerBoxes.getPlugin(BetterShulkerBoxes.class);
        this.cfgi = plugin.cfgi;
    }
}
