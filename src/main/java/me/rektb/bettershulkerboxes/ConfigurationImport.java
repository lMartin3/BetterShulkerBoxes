package me.rektb.bettershulkerboxes;

import org.bukkit.ChatColor;

public class ConfigurationImport {
    BetterShulkerBoxes plugin = BetterShulkerBoxes.getPlugin(BetterShulkerBoxes.class);
    String errors;

    public final String cfg_prefix = plugin.getConfig().getString("prefix");
    public final String cfg_invname = plugin.getConfig().getString("inventory_name");
    public final String cfg_openmsg = plugin.getConfig().getString("open_msg");
    public final String cfg_closemsg = plugin.getConfig().getString("close_msg");
    public final String cfg_nopermsmsg = plugin.getConfig().getString("no_permission_msg");
    public final String cfg_cooldownmsg = plugin.getConfig().getString("cooldown_msg");
    public String prefix = ChatColor.translateAlternateColorCodes('&', cfg_prefix);
    public String invname = ChatColor.translateAlternateColorCodes('&', cfg_invname);
    public String openmsg = ChatColor.translateAlternateColorCodes('&', cfg_openmsg);
    public String closemsg = ChatColor.translateAlternateColorCodes('&', cfg_closemsg);
    public String nopermsmsg = ChatColor.translateAlternateColorCodes('&', cfg_nopermsmsg);
    public String cooldownmsg = ChatColor.translateAlternateColorCodes('&', cfg_cooldownmsg);
    public final int cfg_cooldown = plugin.getConfig().getInt("cooldown");

    public final String cfg_opensound = plugin.getConfig().getString("open_sound");
    public final String cfg_closesound = plugin.getConfig().getString("close_sound");
    public final boolean cfg_opensound_enabled = getEnabled("open_sound");
    public final boolean cfg_closesound_enabled = getEnabled("close_sound");


    public final boolean cfg_enablecooldown = plugin.getConfig().getBoolean("cooldown_enabled");
    public final boolean cfg_requiresperms = plugin.getConfig().getBoolean("requires_permission");
    public final boolean cfg_rightclick_enabled = plugin.getConfig().getBoolean("enable_rightclick_open");
    public final boolean cfg_rightclick_requiresperms = plugin.getConfig().getBoolean("rightclick_requires_permission");

    public final boolean cfg_openmsg_enabled = getEnabled("open_msg");
    public final boolean cfg_closemsg_enabled = getEnabled("close_msg");
    public final boolean cfg_nopermsmsg_enabled = getEnabled("no_permission_msg");
    public final boolean cfg_cooldoenmsg_enabled = getEnabled("cooldown_msg");

    public String checkConfigurationValidity() {
        errors = "";
        if (plugin.getConfig().getString("prefix") == null) {
            errors += "invalid prefix -";
        }
        if (plugin.getConfig().getString("prefix") == null) {
            errors += "invalid inventory_name -";
        }
        if (plugin.getConfig().getString("prefix") == null) {
            errors += "invalid open_msg (use 'none' to disable) -";
        }
        if (plugin.getConfig().getString("prefix") == null) {
            errors += "invalid close_msg (use 'none' to disable) -";
        }
        if (plugin.getConfig().getString("prefix") == null) {
            errors += "invalid no_permission_msg (use 'none' to disable) -";
        }
        if (plugin.getConfig().getString("prefix") == null) {
            errors += "invalid cooldown_msg (use 'none' to disable) -";
        }
        if (plugin.getConfig().getString("prefix") == null) {
            errors += "invalid open_sound (use 'none' to disable) -";
        }
        if (plugin.getConfig().getString("prefix") == null) {
            errors += "invalid close_sound (use 'none' to disable) -";
        }
        if (plugin.getConfig().getString("cooldown_enabled") == null) {
            errors += "invalid cooldown_enabled -";
        }
        if (plugin.getConfig().getString("requires_permission") == null) {
            errors += "invalid requires_permission";
        }
        return errors;
    }

    private boolean getEnabled(String s) {
        if (plugin.getConfig().getString(s) == null) {
            return false;
        }
        return !plugin.getConfig().getString(s).equals("none");
    }
}
