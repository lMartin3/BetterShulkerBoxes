package me.rektb.bettershulkerboxes;

import org.bukkit.ChatColor;

public class ConfigurationImport {
    // this is not spaghetti, it's lasagna!

    BetterShulkerBoxes plugin = BetterShulkerBoxes.getPlugin(BetterShulkerBoxes.class);
    String errors = "";
    String checkfor = "prefix inventory_name open_msg close_msg no_permission_msg cooldown_msg config_reload_msg config_reload_error_msg";

    public final String cfg_prefix = plugin.getConfig().getString("prefix");
    public final String cfg_invname = plugin.getConfig().getString("inventory_name");
    public final String cfg_openmsg = plugin.getConfig().getString("open_msg");
    public final String cfg_closemsg = plugin.getConfig().getString("close_msg");
    public final String cfg_nopermsmsg = plugin.getConfig().getString("no_permission_msg");
    public final String cfg_cooldownmsg = plugin.getConfig().getString("cooldown_msg");
    public final String cfg_configreloadmsg = plugin.getConfig().getString("config_reload_msg");
    public final String cfg_configreloaderrormsg = plugin.getConfig().getString("config_reload_error_msg");
    public String prefix = ChatColor.translateAlternateColorCodes('&', cfg_prefix);
    public String invname = ChatColor.translateAlternateColorCodes('&', cfg_invname);
    public String openmsg = ChatColor.translateAlternateColorCodes('&', cfg_openmsg);
    public String closemsg = ChatColor.translateAlternateColorCodes('&', cfg_closemsg);
    public String nopermsmsg = ChatColor.translateAlternateColorCodes('&', cfg_nopermsmsg);
    public String cooldownmsg = ChatColor.translateAlternateColorCodes('&', cfg_cooldownmsg);
    public String configreload = ChatColor.translateAlternateColorCodes('&', cfg_configreloadmsg);
    public String configreloaderror = ChatColor.translateAlternateColorCodes('&', cfg_configreloaderrormsg);
    public final int cfg_cooldown = plugin.getConfig().getInt("cooldown");

    public final String cfg_opensound = plugin.getConfig().getString("open_sound");
    public final String cfg_closesound = plugin.getConfig().getString("close_sound");
    public final boolean cfg_opensound_enabled = getEnabled("open_sound");
    public final boolean cfg_closesound_enabled = getEnabled("close_sound");


    public final boolean cfg_enablecooldown = plugin.getConfig().getBoolean("cooldown_enabled");
    public final boolean cfg_requiresperms = plugin.getConfig().getBoolean("requires_permission");
    public final boolean cfg_rclickair = plugin.getConfig().getBoolean("enable_rclick_air_open");
    public final boolean cfg_read_only = plugin.getConfig().getBoolean("enable_read_only");
    public final boolean cfg_rightclick_enabled = plugin.getConfig().getBoolean("enable_rightclick_open");
    public final boolean cfg_rightclick_requiresperms = plugin.getConfig().getBoolean("rightclick_requires_permission");
    public final boolean cfg_statistics = plugin.getConfig().getBoolean("enable_statistics");
    public final boolean cfg_rightclick_chest_enabled = plugin.getConfig().getBoolean("enable_rclick_chest_open");

    public final boolean cfg_openmsg_enabled = getEnabled("open_msg");
    public final boolean cfg_closemsg_enabled = getEnabled("close_msg");
    public final boolean cfg_nopermsmsg_enabled = getEnabled("no_permission_msg");
    public final boolean cfg_cooldoenmsg_enabled = getEnabled("cooldown_msg");


    public String checkConfigurationValidity() {
        String[] cfl = checkfor.split(" ");
        for (int i = 0; i < cfl.length; i++) {
            checkStringValidity(cfl[i]);
        }
        return errors;
    }

    private boolean checkStringValidity(String str) {
        if (plugin.getConfig().getString(str) == null) {
            errors += String.format("invalid %s -", str);
            return false;
        }
        return true;
    }

    private boolean getEnabled(String s) {
        if (plugin.getConfig().getString(s) == null) {
            return false;
        }
        return !plugin.getConfig().getString(s).equals("none");
    }
}
