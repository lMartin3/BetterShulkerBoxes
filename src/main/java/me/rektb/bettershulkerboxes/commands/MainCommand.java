package me.rektb.bettershulkerboxes.commands;

import me.rektb.bettershulkerboxes.BetterShulkerBoxes;
import me.rektb.bettershulkerboxes.ConfigurationImport;
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
            if ((args.length > 0 && !args[0].equalsIgnoreCase("reload")) || args.length == 0) {
                sender.sendMessage(cfgi.prefix + ChatColor.AQUA + "This server is running " + ChatColor.YELLOW + "Better Shulker Boxes v" + plugin.getDescription().getVersion() + ChatColor.AQUA + ".");
                if (!sender.hasPermission("bettershulkerboxes.reload")) {
                    sender.sendMessage(cfgi.prefix + ChatColor.AQUA + "Use " + ChatColor.YELLOW + "/bsb reload" + ChatColor.AQUA + " to reload the configuration.");
                }
            } else {
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
                plugin.checkConfigValidity();
                plugin.newInstances();
                sender.sendMessage(cfgi.prefix + cfgi.configreload);
            }
        }
        return false;
    }

    public void getNewInstances() {
        this.plugin = BetterShulkerBoxes.getPlugin(BetterShulkerBoxes.class);
        this.cfgi = plugin.cfgi;
    }
}
