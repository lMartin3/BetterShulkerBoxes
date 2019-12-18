package me.rektb.bettershulkerboxes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class ShulkerManage {
    //This class holds the methods to close and open shulker boxes and some helper functions.
    //STILL NOT IN USE
    private BetterShulkerBoxes plugin = BetterShulkerBoxes.getPlugin(BetterShulkerBoxes.class);

    private ConfigurationImport cfgi = plugin.cfgi;
    public ArrayList cooldownlist = new ArrayList();
    public HashMap<String, Integer> swap = new HashMap<>();
    public int finalcooldown = cfgi.cfg_cooldown * 20;
    //Cooldown

    //Close shulkerbox
    public void closeShulker(Player p, ItemStack shulkerStack, Inventory shulkerInventory) {
        String getitemname = shulkerStack.getItemMeta().getDisplayName();
        ItemStack itemStack = new ItemStack(shulkerStack);

        BlockStateMeta bsm = (BlockStateMeta) itemStack.getItemMeta();
        ShulkerBox box = (ShulkerBox) bsm.getBlockState();
        box.getInventory().setContents(shulkerInventory.getContents());
        if (getitemname != " ") {
            bsm.setDisplayName(getitemname);
        }
        bsm.setBlockState(box);
        box.update();
        itemStack.setItemMeta(bsm);
        p.getInventory().setItem(p.getInventory().getHeldItemSlot(), itemStack);
        if (cfgi.cfg_closemsg_enabled) {
            p.sendMessage(cfgi.prefix + cfgi.closemsg.replace("%itemname%", getitemname));
        }
        if (!cfgi.cfg_closesound_enabled) {
            return;
        }
        try {
            p.playSound(p.getLocation(), Sound.valueOf(cfgi.cfg_closesound), 1.0F, 1.0F);
        } catch (Exception el) {
            Bukkit.getServer().getConsoleSender().sendMessage(cfgi.prefix + ChatColor.RED + " <ERROR> Invalid sound: " + ChatColor.YELLOW + cfgi.cfg_closesound);
            p.playSound(p.getLocation(), Sound.BLOCK_SHULKER_BOX_CLOSE, 1.0F, 1.0F);
            p.sendMessage(cfgi.prefix + ChatColor.RED + " <ERROR> Invalid sound: " + ChatColor.YELLOW + cfgi.cfg_closesound + ChatColor.RED + ", please inform an administrator about this.");
        }
    }


    //Open shulkerbox
    public void openShulker(Player p, ItemStack shulkerStack, int slot) {
        BlockStateMeta im = (BlockStateMeta) shulkerStack.getItemMeta();
        ShulkerBox shulker = (ShulkerBox) im.getBlockState();
        String invname = "";

        if (shulkerStack.hasItemMeta() && shulkerStack.getItemMeta().hasDisplayName()) {
            invname = shulkerStack.getItemMeta().getDisplayName();
        }

        if (cfgi.cfg_openmsg_enabled) {
            p.sendMessage(cfgi.prefix + cfgi.openmsg.replace("%itemname%", im.getDisplayName()));
        }

        invname = cfgi.invname.replace("%itemname%", im.getDisplayName());

        Inventory inv;
        if (invname.equals("") || invname.equals(" ")) {
            inv = Bukkit.createInventory(null, InventoryType.SHULKER_BOX, InventoryType.SHULKER_BOX.getDefaultTitle());
        } else {
            inv = Bukkit.createInventory(null, InventoryType.SHULKER_BOX, invname);
        }

        inv.setContents(shulker.getInventory().getContents());
        p.openInventory(inv);
        p.getInventory().setHeldItemSlot(slot);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
            public void run() {
                cooldownlist.remove(p.getName());
            }
        }, finalcooldown);

        if (!cfgi.cfg_opensound_enabled) {
            return;
        }
        try {
            p.playSound(p.getLocation(), Sound.valueOf(cfgi.cfg_opensound), 1.0F, 1.0F);
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(cfgi.prefix + ChatColor.RED + " <ERROR> Invalid sound: " + ChatColor.YELLOW + cfgi.cfg_opensound);
            p.playSound(p.getLocation(), Sound.BLOCK_SHULKER_BOX_OPEN, 1.0F, 1.0F);
            p.sendMessage(cfgi.prefix + ChatColor.RED + " <ERROR> Invalid sound: " + ChatColor.YELLOW + cfgi.cfg_opensound + ChatColor.RED + ", please inform an administrator about this.");
        }
    }


    // Just to check if the player is holding a shulkerbox
    public boolean isHoldingShulker(ItemStack eventItem) {
        if (!(eventItem.getItemMeta() instanceof BlockStateMeta)) {
            return false;
        }
        BlockStateMeta im = (BlockStateMeta) eventItem.getItemMeta();
        if (!(im.getBlockState() instanceof ShulkerBox)) {
            return false;
        }
        return eventItem.toString().contains("SHULKER_BOX");
    }

    public boolean isInventoryShulker(ItemStack shulker, String inventoryname) {
        String checkname = cfgi.invname;
        String holdingitemname = "";
        if (shulker.getItemMeta() != null && !shulker.getItemMeta().getDisplayName().isEmpty()) {
            holdingitemname = shulker.getItemMeta().getDisplayName();
        }
        checkname = checkname.replace("%itemname%", holdingitemname);
        if (checkname.isEmpty()) {
            checkname = InventoryType.SHULKER_BOX.getDefaultTitle();
        }
        return inventoryname.equals(checkname);
    }

    public void shulkerSwap(Player p, int slot) {
        ItemStack shulker = p.getInventory().getItem(slot);
        ItemStack originalmh = p.getInventory().getItemInMainHand();
        swap.put(p.getName(), slot);
        p.getInventory().setItem(slot, originalmh);
        p.getInventory().setItem(p.getInventory().getHeldItemSlot(), shulker);
    }

    public void shulkerUnswap(Player p) {
        if (!swap.containsKey(p.getName())) {
            return;
        }
        ItemStack shulker = p.getInventory().getItemInMainHand();
        ItemStack tomain = p.getInventory().getItem(swap.get(p.getName()));
        p.getInventory().setItem(p.getInventory().getHeldItemSlot(), tomain);
        p.getInventory().setItem(swap.get(p.getName()), shulker);
        swap.remove(p.getName());
    }

    public void getNewInstances() {
        this.plugin = BetterShulkerBoxes.getPlugin(BetterShulkerBoxes.class);
        this.cfgi = plugin.cfgi;
    }

}
