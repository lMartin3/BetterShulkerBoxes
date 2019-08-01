package me.rektb.bettershulkerboxes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.ArrayList;

public class ShulkerManage {
    //This class holds the methods to close and open shulker boxes and some helper functions.
    //STILL NOT IN USE
    private BetterShulkerBoxes plugin;

    public ShulkerManage(BetterShulkerBoxes pl) {
        this.plugin = pl;
    }

    private ConfigurationImport cfgim = plugin.cfgi;
    public ArrayList<String> cooldownlist = new ArrayList();
    public int finalcooldown = cfgim.cfg_cooldown * 20;
    //Cooldown

    //Close shulkerbox
    public void closeShulker(Player p, ItemStack shulkerStack, Inventory shulkerInventory) {
        String getitemname = shulkerStack.getItemMeta().getDisplayName();
        ItemStack itemStack = new ItemStack(shulkerStack);

        BlockStateMeta bsm = (BlockStateMeta) itemStack.getItemMeta();
        ShulkerBox box = (ShulkerBox) bsm.getBlockState();
        box.getInventory().setContents(shulkerInventory.getContents());
        if (getitemname != "") {
            bsm.setDisplayName(getitemname);
        }
        bsm.setBlockState(box);
        box.update();
        itemStack.setItemMeta(bsm);
        p.getInventory().setItem(p.getInventory().getHeldItemSlot(), itemStack);
        if (cfgim.cfg_closemsg_enabled) {
            p.sendMessage(cfgim.prefix + cfgim.closemsg);
        }
        try {
            p.playSound(p.getLocation(), Sound.valueOf(cfgim.cfg_closesound), 1.0F, 1.0F);
        } catch (Exception el) {
            Bukkit.getServer().getConsoleSender().sendMessage(cfgim.prefix + ChatColor.RED + " <ERROR> Invalid sound: " + ChatColor.YELLOW + cfgim.cfg_closesound);
            p.playSound(p.getLocation(), Sound.BLOCK_SHULKER_BOX_CLOSE, 1.0F, 1.0F);
            p.sendMessage(cfgim.prefix + ChatColor.RED + " <ERROR> Invalid sound: " + ChatColor.YELLOW + cfgim.cfg_closesound + ChatColor.RED + ", please inform an administrator about this.");
        }
    }


    //Open shulkerbox
    public void openShulker(Player p, ItemStack shulkerStack) {
        BlockStateMeta im = (BlockStateMeta) shulkerStack.getItemMeta();
        ShulkerBox shulker = (ShulkerBox) im.getBlockState();
        String invname = "";
        if (shulkerStack.hasItemMeta() && shulkerStack.getItemMeta().hasDisplayName()) {
            invname = shulkerStack.getItemMeta().getDisplayName();
        }
        if (cfgim.cfg_openmsg_enabled) {
            p.sendMessage(cfgim.prefix + cfgim.openmsg);
        }
        if (cfgim.invname.contains("%itemname%")) {
            if (shulkerStack.getItemMeta() != null) {
                if (shulkerStack.getItemMeta().hasDisplayName()) {
                    cfgim.invname = invname.replace("%itemname%", invname);
                }
            }
        }
        Inventory inv = Bukkit.createInventory(null, 27, invname);
        inv.setContents(shulker.getInventory().getContents());
        p.openInventory(inv);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
            public void run() {
                cooldownlist.remove(p.getName());
            }
        }, finalcooldown);
        try {
            p.playSound(p.getLocation(), Sound.valueOf(cfgim.cfg_opensound), 1.0F, 1.0F);
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(cfgim.prefix + ChatColor.RED + " <ERROR> Invalid sound: " + ChatColor.YELLOW + cfgim.cfg_opensound);
            p.playSound(p.getLocation(), Sound.BLOCK_SHULKER_BOX_OPEN, 1.0F, 1.0F);
            p.sendMessage(cfgim.prefix + ChatColor.RED + " <ERROR> Invalid sound: " + ChatColor.YELLOW + cfgim.cfg_opensound + ChatColor.RED + ", please inform an administrator about this.");
        }
    }
}
