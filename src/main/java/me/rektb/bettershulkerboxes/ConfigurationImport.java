package me.rektb.bettershulkerboxes;

import org.bukkit.ChatColor;

public class ConfigurationImport {
    BetterShulkerBoxes plugin;

    public ConfigurationImport(BetterShulkerBoxes pl) {
        this.plugin = pl;
    }

    public final String cfg_prefix = this.plugin.getConfig().getString("prefix");
    public final String cfg_invname = this.plugin.getConfig().getString("inventory_name");
    public final String cfg_openmsg = this.plugin.getConfig().getString("open_msg");
    public final String cfg_closemsg = this.plugin.getConfig().getString("close_msg");
    public final String cfg_nopermsmsg = this.plugin.getConfig().getString("no_permission_msg");
    public final String cfg_cooldownmsg = this.plugin.getConfig().getString("cooldown_msg");
    public String prefix = ChatColor.translateAlternateColorCodes('&', this.cfg_prefix);
    public String invname = ChatColor.translateAlternateColorCodes('&', this.cfg_invname);
    public String openmsg = ChatColor.translateAlternateColorCodes('&', this.cfg_openmsg);
    public String closemsg = ChatColor.translateAlternateColorCodes('&', this.cfg_closemsg);
    public String nopermsmsg = ChatColor.translateAlternateColorCodes('&', this.cfg_nopermsmsg);
    public String cooldownmsg = ChatColor.translateAlternateColorCodes('&', this.cfg_cooldownmsg);
    public final int cfg_cooldown = this.plugin.getConfig().getInt("cooldown");
    public final String cfg_opensound = this.plugin.getConfig().getString("open_sound");
    public final String cfg_closesound = this.plugin.getConfig().getString("close_sound");
    public final boolean cfg_enablecooldown = this.plugin.getConfig().getBoolean("cooldown_enabled");
    public final boolean cfg_requiresperms = this.plugin.getConfig().getBoolean("requires_permission");
    public final boolean cfg_openmsg_enabled = this.plugin.getConfig().getBoolean("open_msg_enabled");
    public final boolean cfg_closemsg_enabled = this.plugin.getConfig().getBoolean("close_msg_enabled");
    public final boolean cfg_nopermsmsg_enabled = this.plugin.getConfig().getBoolean("no_permission_msg_enabled");
    public final boolean cfg_cooldoenmsg_enabled = this.plugin.getConfig().getBoolean("cooldown_msg_enabled");
}
