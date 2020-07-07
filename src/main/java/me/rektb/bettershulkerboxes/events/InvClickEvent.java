package me.rektb.bettershulkerboxes.events;

import me.rektb.bettershulkerboxes.BetterShulkerBoxes;
import me.rektb.bettershulkerboxes.utils.ConfigurationImport;
import me.rektb.bettershulkerboxes.utils.ShulkerManage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class InvClickEvent implements Listener {

    private final ConfigurationImport cfgi;
    private final ShulkerManage shlkm;

    public InvClickEvent(BetterShulkerBoxes plugin) {
        this.cfgi = plugin.cfgi;
        this.shlkm = plugin.shlkm;
    }

    @EventHandler
    public void inventoryClickInShulkerbox(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (!cfgi.cfg_read_only) {
            return;
        }
        if (!e.getInventory().getType().equals(InventoryType.SHULKER_BOX)) {
            return;
        }
        if (!shlkm.isHoldingShulker(p.getInventory().getItemInMainHand())) {
            return;
        }
        if (!shlkm.isInventoryShulker(p.getInventory().getItemInMainHand(), e.getView().getTitle())) {
            return;
        }
        e.setCancelled(true);

    }
}
