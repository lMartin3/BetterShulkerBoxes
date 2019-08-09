package me.rektb.bettershulkerboxes.events;

import me.rektb.bettershulkerboxes.BetterShulkerBoxes;
import me.rektb.bettershulkerboxes.ConfigurationImport;
import me.rektb.bettershulkerboxes.ShulkerManage;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.ArrayList;

public class InteractEvent implements Listener {
    private BetterShulkerBoxes plugin = BetterShulkerBoxes.getPlugin(BetterShulkerBoxes.class);
    private ConfigurationImport cfgi = plugin.cfgi;
    private ShulkerManage shlkm;
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
        if ((e.getAction().equals(Action.RIGHT_CLICK_AIR)) && ShulkerManage.isHoldingShulker(p, e.getItem())) {
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
                    (this.cooldownlist.contains(p)) &&
                    (!p.hasPermission("bettershulkerboxes.bypasscooldown"))) {
                if (cfgi.cfg_cooldoenmsg_enabled) {
                    p.sendMessage(cfgi.prefix + cfgi.cooldownmsg);
                }
                return;
            }
            String getitemname = holding.getItemMeta().getDisplayName();
            plugin.shlkm.openShulker(p, holding);
            //shulkerOpen(p, shulker, getitemname);
        }
    }


    //Right click in inventory to open
    public void rightClickInventory(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        e.getClickedInventory();
    }
}
