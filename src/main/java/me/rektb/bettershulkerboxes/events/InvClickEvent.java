package me.rektb.bettershulkerboxes.events;

import me.rektb.bettershulkerboxes.BetterShulkerBoxes;
import me.rektb.bettershulkerboxes.ConfigurationImport;
import me.rektb.bettershulkerboxes.ShulkerManage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InvClickEvent implements Listener {

    private BetterShulkerBoxes plugin;
    private ConfigurationImport cfgi;
    private ShulkerManage shlkm;

    public InvClickEvent(BetterShulkerBoxes plugin) {
        this.plugin = plugin;
        this.cfgi = plugin.cfgi;
        this.shlkm = plugin.shlkm;
    }

    @EventHandler
    public void inventoryClickInShulkerbox(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        p.sendMessage(e.getClick().toString());
        p.sendMessage(e.getClickedInventory().getType().toString());
        p.sendMessage(e.getAction().toString());
        p.sendMessage(e.getAction().toString());
        if (!cfgi.cfg_read_only) {
            return;
        }
        if (!shlkm.isHoldingShulker(p.getInventory().getItemInMainHand())) {
            return;
        }
        if (!shlkm.isInventoryShulker(p.getInventory().getItemInMainHand(), e.getView().getTitle())) {
            return;
        }

    }
}
