package me.rektb.bettershulkerboxes.events;

import me.rektb.bettershulkerboxes.BetterShulkerBoxes;
import me.rektb.bettershulkerboxes.utils.ConfigurationImport;
import me.rektb.bettershulkerboxes.utils.ShulkerManage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class InvCloseEvent implements Listener {
    private BetterShulkerBoxes plugin = BetterShulkerBoxes.getPlugin(BetterShulkerBoxes.class);
    private ConfigurationImport cfgi = plugin.cfgi;
    private final ShulkerManage shlkm = plugin.shlkm;

    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();

        // This prevents duping when a player closes a (block) shulker box while holding one with the same
        // color and name.
        if (e.getInventory().getHolder() != null && !e.getInventory().getHolder().equals(p)) {
            return;
        }

        String nowinvname = e.getView().getTitle();
        if (shlkm.isHoldingShulker(p.getInventory().getItemInMainHand())) {
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
                Material mat = Material.AIR;
                try {
                    mat = p.getInventory().getItem(shlkm.swap.get(p.getName())).getType();
                } catch (NullPointerException npe) {
                    //Do nothing about it
                }
                if (!mat.toString().contains("SHULKER_BOX")) {
                    shlkm.shulkerUnswap(p);
                }

            }
        }
    }

    // This updates the instances so the bsb reload command works.
    public void getNewInstances() {
        this.plugin = BetterShulkerBoxes.getPlugin(BetterShulkerBoxes.class);
        this.cfgi = plugin.cfgi;
    }
}
