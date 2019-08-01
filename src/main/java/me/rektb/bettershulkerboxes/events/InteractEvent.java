package me.rektb.bettershulkerboxes.events;

import me.rektb.bettershulkerboxes.BetterShulkerBoxes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class InteractEvent
        implements Listener {
    ArrayList<String> cooldownlist = new ArrayList();
    Plugin plugin = BetterShulkerBoxes.getPlugin(BetterShulkerBoxes.class);
    private final String cfg_prefix = this.plugin.getConfig().getString("prefix");
    private final String cfg_invname = this.plugin.getConfig().getString("inventory_name");
    private final String cfg_openmsg = this.plugin.getConfig().getString("open_msg");
    private final String cfg_closemsg = this.plugin.getConfig().getString("close_msg");
    private final String cfg_nopermsmsg = this.plugin.getConfig().getString("no_permission_msg");
    private final String cfg_cooldownmsg = this.plugin.getConfig().getString("cooldown_msg");
    private String prefix = ChatColor.translateAlternateColorCodes('&', this.cfg_prefix);
    private String invname = ChatColor.translateAlternateColorCodes('&', this.cfg_invname);
    private String openmsg = ChatColor.translateAlternateColorCodes('&', this.cfg_openmsg);
    private String closemsg = ChatColor.translateAlternateColorCodes('&', this.cfg_closemsg);
    private String nopermsmsg = ChatColor.translateAlternateColorCodes('&', this.cfg_nopermsmsg);
    private String cooldownmsg = ChatColor.translateAlternateColorCodes('&', this.cfg_cooldownmsg);
    private final int cfg_cooldown = this.plugin.getConfig().getInt("cooldown");
    private final String cfg_opensound = this.plugin.getConfig().getString("open_sound");
    private final String cfg_closesound = this.plugin.getConfig().getString("close_sound");
    private final boolean cfg_enablecooldown = this.plugin.getConfig().getBoolean("cooldown_enabled");
    private final boolean cfg_requiresperms = this.plugin.getConfig().getBoolean("requires_permission");
    private final boolean cfg_openmsg_enabled = this.plugin.getConfig().getBoolean("open_msg_enabled");
    private final boolean cfg_closemsg_enabled = this.plugin.getConfig().getBoolean("close_msg_enabled");
    private final boolean cfg_nopermsmsg_enabled = this.plugin.getConfig().getBoolean("no_permission_msg_enabled");
    private final boolean cfg_cooldoenmsg_enabled = this.plugin.getConfig().getBoolean("cooldown_msg_enabled");
    private int finalcooldown = this.cfg_cooldown * 20;

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.getInventory().getItemInMainHand().equals(Material.AIR)) {
            return;
        }
        if (!e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }
        ItemStack holding = p.getInventory().getItemInMainHand();
        if (p.getInventory().getItemInOffHand().equals(e.getItem())) {
            return;
        }
        ItemStack eitem = e.getItem();
        if (!(holding.getItemMeta() instanceof BlockStateMeta)) {
            return;
        }
        BlockStateMeta im = (BlockStateMeta) eitem.getItemMeta();
        if (!(im.getBlockState() instanceof ShulkerBox)) {
            return;
        }
        ShulkerBox shulker = (ShulkerBox) im.getBlockState();
        if ((e.getAction().equals(Action.RIGHT_CLICK_AIR)) &&
                (holding.toString().contains("SHULKER_BOX"))) {
            e.setCancelled(true);
            if (this.cfg_requiresperms) {
                if (!p.hasPermission("bettershulkerboxes.use")) {
                    if (this.cfg_nopermsmsg_enabled) {
                        p.sendMessage(this.prefix + this.nopermsmsg);
                    }
                    return;
                }
            }
            if ((this.cfg_enablecooldown) &&
                    (this.cooldownlist.contains(p)) &&
                    (!p.hasPermission("bettershulkerboxes.bypasscooldown"))) {
                if (this.cfg_cooldoenmsg_enabled) {
                    p.sendMessage(this.prefix + this.cooldownmsg);
                }
                return;
            }
            String getitemname = e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName();
            shulkerOpen(p, shulker, getitemname);
        }
    }

    //Right click in inventory to open
    public void rightClickInventory(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        e.getClickedInventory();
    }



    public void shulkerOpen(final Player p, ShulkerBox shulker, String getitemname) {
        if ((!p.getOpenInventory().getType().equals(InventoryType.CREATIVE)) && (!p.getOpenInventory().getType().equals(InventoryType.CRAFTING))) {
            return;
        }
        if (this.cfg_openmsg_enabled) {
            p.sendMessage(this.prefix + this.openmsg);
        }
        p.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
        if (this.invname.contains("%itemname%")) {
            if (getitemname != "") {
                this.invname = this.invname.replace("%itemname%", getitemname);
            } else {
                this.invname = this.invname.replace("%itemname%", "&7Shulkerbox");
            }
        }
        Inventory inv = Bukkit.createInventory(null, 27, this.invname);
        inv.setContents(shulker.getInventory().getContents());
        p.openInventory(inv);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
            public void run() {
                InteractEvent.this.cooldownlist.remove(p.getName());
            }
        }, this.finalcooldown);
        try {
            p.playSound(p.getLocation(), Sound.valueOf(this.cfg_opensound), 1.0F, 1.0F);
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(this.prefix + ChatColor.RED + " <ERROR> Invalid sound: " + ChatColor.YELLOW + this.cfg_opensound);
            p.playSound(p.getLocation(), Sound.BLOCK_SHULKER_BOX_OPEN, 1.0F, 1.0F);
            p.sendMessage(this.prefix + ChatColor.RED + " <ERROR> Invalid sound: " + ChatColor.YELLOW + this.cfg_opensound + ChatColor.RED + ", please inform an administrator about this.");
        }
    }
}
