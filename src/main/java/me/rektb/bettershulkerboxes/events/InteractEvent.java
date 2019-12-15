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
    ArrayList<String> cooldownlist = new ArrayList();
    private int finalcooldown = cfgi.cfg_cooldown * 20;

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
        if ((e.getAction().equals(Action.RIGHT_CLICK_AIR)) && shlkm.isHoldingShulker(p, e.getItem())) {
            e.setCancelled(true);
            if (cfgi.cfg_requiresperms) {
                if (!p.hasPermission("bettershulkerboxes.use")) {
                    if (cfgi.cfg_nopermsmsg_enabled) {
                        p.sendMessage(cfgi.prefix + cfgi.nopermsmsg);
                    }
                    return;
                }
            }
            if ((cfgi.cfg_enablecooldown) &&
                    (this.cooldownlist.contains(p.getName())) &&
                    (!p.hasPermission("bettershulkerboxes.bypasscooldown"))) {
                if (cfgi.cfg_cooldoenmsg_enabled) {
                    p.sendMessage(cfgi.prefix + cfgi.cooldownmsg);
                }
                return;
            }
            cooldownlist.add(p.getName());
            removeCooldownLater(p);
            String getitemname = holding.getItemMeta().getDisplayName();
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
        //CHANGED
        boolean invshulk = shlkm.isInventoryShulker(p.getInventory().getItemInMainHand(), p.getOpenInventory().getTitle());
        if (!(p.getOpenInventory().getType().equals(InventoryType.CRAFTING) || invshulk || p.getOpenInventory().getType().equals(InventoryType.CREATIVE))) {
            return;
        }
        if (e.getCurrentItem() == null) {
            return;
        }
        if (!shlkm.isHoldingShulker(p, e.getCurrentItem())) {
            return;
        }
        //CHANGED
        if (!p.getOpenInventory().getType().equals(InventoryType.CRAFTING) && e.getSlot() == p.getInventory().getHeldItemSlot()) {
            return;
        }

        //from here, player is 100% trying to open the shulker box;
        e.setCancelled(true);

        //TODO helper function for this
        if (!cfgi.cfg_rightclick_enabled) {
            return;
        }

        if (cfgi.cfg_rightclick_requiresperms && !p.hasPermission("bettershulkerboxes.use.rightclick")) {
            if (cfgi.cfg_nopermsmsg_enabled) {
                p.sendMessage(cfgi.prefix + cfgi.nopermsmsg);
            }
            return;
        }

        if (cfgi.cfg_requiresperms && !p.hasPermission("bettershulkerboxes.use")) {
            if (cfgi.cfg_nopermsmsg_enabled) {
                p.sendMessage(cfgi.prefix + cfgi.nopermsmsg);
            }
            return;
        }

        if ((cfgi.cfg_enablecooldown) &&
                (this.cooldownlist.contains(p.getName())) &&
                (!p.hasPermission("bettershulkerboxes.bypasscooldown"))) {
            if (cfgi.cfg_cooldoenmsg_enabled) {
                p.sendMessage(cfgi.prefix + cfgi.cooldownmsg);
            }
            return;
        }
        cooldownlist.add(p.getName());
        removeCooldownLater(p);
        //CHANGED
        if (invshulk){
          shlkm.closeShulker(p, p.getInventory().getItemInMainHand(), e.getInventory());
        }

        shlkm.openShulker(p, p.getInventory().getItem(e.getSlot()), p.getInventory().getHeldItemSlot());
        shlkm.shulkerSwap(p, e.getSlot());
        e.getClickedInventory();
    }


    public void removeCooldownLater(Player p) {
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                cooldownlist.remove(p.getName());
            }
        }, (cfgi.cfg_cooldown / 1000) * 20);
    }

    public void getNewInstances() {
        this.plugin = BetterShulkerBoxes.getPlugin(BetterShulkerBoxes.class);
        this.cfgi = plugin.cfgi;
    }
}
