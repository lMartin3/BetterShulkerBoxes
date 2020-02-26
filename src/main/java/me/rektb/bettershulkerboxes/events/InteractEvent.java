package me.rektb.bettershulkerboxes.events;

import me.rektb.bettershulkerboxes.BetterShulkerBoxes;
import me.rektb.bettershulkerboxes.ConfigurationImport;
import me.rektb.bettershulkerboxes.ShulkerManage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.ArrayList;

public class InteractEvent implements Listener {
    private BetterShulkerBoxes plugin = BetterShulkerBoxes.getPlugin(BetterShulkerBoxes.class);
    private ConfigurationImport cfgi = plugin.cfgi;
    private ShulkerManage shlkm = plugin.shlkm;
    ArrayList<String> cooldownlist = new ArrayList<>();

    public boolean doChecks(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            return false;
        }
        if (!e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return false;
        }
        ItemStack holding = p.getInventory().getItemInMainHand();
        if (p.getInventory().getItemInOffHand().equals(e.getItem())) {
            return false;
        }
        ItemStack eitem = e.getItem();
        if (!(holding.getItemMeta() instanceof BlockStateMeta)) {
            return false;
        }
        assert eitem != null;
        BlockStateMeta im = (BlockStateMeta) eitem.getItemMeta();
        assert im != null;
        return im.getBlockState() instanceof ShulkerBox;
    }


    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!doChecks(e)) {
            return;
        }
        ItemStack holding = p.getInventory().getItemInMainHand();
        //ShulkerBox shulker = (ShulkerBox) im.getBlockState();
        if ((e.getAction().equals(Action.RIGHT_CLICK_AIR)) && shlkm.isHoldingShulker(e.getItem())) {
            if (!cfgi.cfg_rclickair) {
                return;
            }
            e.setCancelled(true);
            if (cfgi.cfg_requiresperms && !p.hasPermission("bettershulkerboxes.use")) {
                if (cfgi.cfg_nopermsmsg_enabled) {
                    p.sendMessage(cfgi.prefix + cfgi.nopermsmsg);
                }
                return;
            }
            if ((cfgi.cfg_enablecooldown) && (this.cooldownlist.contains(p.getName())) && (!p.hasPermission("bettershulkerboxes.bypasscooldown"))) {
                if (cfgi.cfg_cooldoenmsg_enabled) {
                    p.sendMessage(cfgi.prefix + cfgi.cooldownmsg);
                }
                return;
            }
            cooldownlist.add(p.getName());
            removeCooldownLater(p);
            //String getitemname = holding.getItemMeta().getDisplayName();
            int slot = p.getInventory().getHeldItemSlot();
            plugin.shlkm.openShulker(p, holding, slot);
        }
    }


    //Right click in inventory to open
    @EventHandler
    public void rightClickInventory(InventoryClickEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Player p = (Player) e.getWhoClicked();

        if (!e.getClick().equals(ClickType.RIGHT)) {
            return;
        }
        if (e.getClickedInventory() == null) {
            return;
        }

        boolean is_inventory_shulker = false;
        boolean is_performing_switch = false;
        if (p.getOpenInventory().getInventory(1) != null && p.getOpenInventory().getType().equals(InventoryType.SHULKER_BOX)) {
            if (shlkm.isInventoryShulker(p.getInventory().getItemInMainHand(), p.getOpenInventory().getTitle())) {
                is_inventory_shulker = true;
            }
        }
        if (e.getCurrentItem() == null) {
            return;
        }
        if (e.getCurrentItem() != null && is_inventory_shulker && shlkm.isHoldingShulker(e.getCurrentItem())) {
            is_performing_switch = true;
        }
        InventoryType type = e.getClickedInventory().getType();
        if (!(type.equals(InventoryType.PLAYER) || type.equals(InventoryType.CRAFTING) || is_inventory_shulker || type.equals(InventoryType.CHEST) || type.equals(InventoryType.ENDER_CHEST))) {
            return;
        }
        if (!shlkm.isHoldingShulker(e.getCurrentItem())) {
            return;
        }

        int e_slot = e.getSlot();
        //from here, player is 100% trying to open the shulker box;
        if (type.equals(InventoryType.CHEST) || type.equals(InventoryType.ENDER_CHEST)) {
            if (!cfgi.cfg_rightclick_chest_enabled) {
                return;
            }
            ItemStack it = e.getCurrentItem();
            ArrayList<Integer> free_slots = new ArrayList<>();
            for (int i = 0; i < 35; i++) {
                if (p.getInventory().getItem(i) == null) {
                    free_slots.add(i);
                }
            }
            if (free_slots.size() == 0) {
                e.setCancelled(true);
                return;
            }
            //e.getInventory().remove(it);
            int sel_slot = -1;
            for (int i = 0; i < e.getInventory().getSize() - 1; i++) {
                ItemStack slot_stack = e.getInventory().getItem(i);
                if (slot_stack != null && slot_stack.equals(it)) {
                    sel_slot = i;
                    break;
                }
            }
            e.getInventory().setItem(sel_slot, new ItemStack(Material.AIR));
            p.getInventory().setItem(free_slots.get(0), it);
            e_slot = free_slots.get(0);
            //TODO

        }
        e.setCancelled(true);
        if (!canOpen(p)) {
            return;
        }
        cooldownlist.add(p.getName());
        removeCooldownLater(p);


        if (p.getOpenInventory().getInventory(1) != null && is_performing_switch) {
            //Close inventory without performing unswap
            shlkm.swap.remove(p.getName());
            shlkm.closeShulker(p, p.getInventory().getItemInMainHand(), e.getInventory());

        }
        shlkm.shulkerSwap(p, e_slot);
        shlkm.openShulker(p, p.getInventory().getItemInMainHand(), p.getInventory().getHeldItemSlot());
    }


    public void removeCooldownLater(Player p) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> cooldownlist.remove(p.getName()), (cfgi.cfg_cooldown / 1000) * 20);
    }

    public void getNewInstances() {
        this.plugin = BetterShulkerBoxes.getPlugin(BetterShulkerBoxes.class);
        this.cfgi = plugin.cfgi;
    }

    public boolean canOpen(Player p) {
        //TODO helper function for this
        if (!cfgi.cfg_rightclick_enabled) {
            return false;
        }

        if (cfgi.cfg_rightclick_requiresperms && !p.hasPermission("bettershulkerboxes.use.rightclick")) {
            if (cfgi.cfg_nopermsmsg_enabled) {
                p.sendMessage(cfgi.prefix + cfgi.nopermsmsg);
            }
            return false;
        }

        if (cfgi.cfg_requiresperms && !p.hasPermission("bettershulkerboxes.use")) {
            if (cfgi.cfg_nopermsmsg_enabled) {
                p.sendMessage(cfgi.prefix + cfgi.nopermsmsg);
            }
            return false;
        }

        if ((cfgi.cfg_enablecooldown) &&
                (this.cooldownlist.contains(p.getName())) &&
                (!p.hasPermission("bettershulkerboxes.bypasscooldown"))) {
            if (cfgi.cfg_cooldoenmsg_enabled) {
                p.sendMessage(cfgi.prefix + cfgi.cooldownmsg);
            }
            return false;
        }
        return true;
    }
}
