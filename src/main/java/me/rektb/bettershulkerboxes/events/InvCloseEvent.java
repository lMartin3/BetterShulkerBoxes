package me.rektb.bettershulkerboxes.events;

import me.rektb.bettershulkerboxes.BetterShulkerBoxes;
import me.rektb.bettershulkerboxes.ConfigurationImport;
import me.rektb.bettershulkerboxes.ShulkerManage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class InvCloseEvent implements Listener {
    private BetterShulkerBoxes plugin = BetterShulkerBoxes.getPlugin(BetterShulkerBoxes.class);
    ConfigurationImport cfgi = plugin.cfgi;
    ShulkerManage shlkm = plugin.shlkm;
    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        String nowinvname = e.getView().getTitle();
        if (shlkm.isHoldingShulker(p, p.getInventory().getItemInMainHand())) {
            Material holdingshulker = e.getPlayer().getInventory().getItemInMainHand().getType();
            String holdingitemname = "";
            ItemStack iteminmainhand = e.getPlayer().getInventory().getItemInMainHand();
            if (iteminmainhand.getItemMeta() != null && iteminmainhand.getItemMeta().hasDisplayName()) {
                holdingitemname = iteminmainhand.getItemMeta().getDisplayName();
            }

            String checkname = cfgi.invname;
            if (holdingitemname.isEmpty()) {
                checkname = cfgi.invname.replace("%itemname%", holdingitemname);
                if (checkname.isEmpty()) {
                    checkname = InventoryType.SHULKER_BOX.getDefaultTitle();
                }
            } else {
                checkname = checkname.replace("%itemname%", holdingitemname);
            }

            if (nowinvname.equalsIgnoreCase(checkname)) {
                shlkm.closeShulker(p, e.getPlayer().getInventory().getItemInMainHand(), e.getInventory());
                shlkm.shulkerUnsawp(p);
            }
        }
    }

    public void getNewInstances() {
        this.plugin = BetterShulkerBoxes.getPlugin(BetterShulkerBoxes.class);
        this.cfgi = plugin.cfgi;
    }
}
