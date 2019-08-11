package me.rektb.bettershulkerboxes.events;

import me.rektb.bettershulkerboxes.BetterShulkerBoxes;
import me.rektb.bettershulkerboxes.ConfigurationImport;
import me.rektb.bettershulkerboxes.ShulkerManage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class DupePreventEvents implements Listener {
    private BetterShulkerBoxes plugin = BetterShulkerBoxes.getPlugin(BetterShulkerBoxes.class);
    private ConfigurationImport cfgi = plugin.cfgi;
    private ShulkerManage shlkm = plugin.shlkm;

    @EventHandler
    public void dropDupe(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        ItemStack idr = e.getItemDrop().getItemStack();
        if (shlkm.isInventoryShulker(idr, p.getOpenInventory().getTitle()) && (e.getItemDrop().getItemStack().toString().contains("SHULKER_BOX"))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void deathDupe(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getEntity();
        if (p.getHealth() - e.getDamage() > 0.0D) {
            return;
        }
        if (shlkm.isInventoryShulker(p.getInventory().getItemInMainHand(), p.getOpenInventory().getTitle()) && (p.getInventory().getItemInMainHand().toString().contains("SHULKER_BOX"))) {
            shlkm.closeShulker(p, p.getInventory().getItemInMainHand(), p.getOpenInventory().getTopInventory());
        }
    }


    //TODO nesting here
    @EventHandler
    public void invalidPrevent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == null) {
            return;
        }
        if (e.getCurrentItem() == null) {
            return;
        }
        if (!shlkm.isInventoryShulker(p.getInventory().getItemInMainHand(), p.getOpenInventory().getTitle())) {
            return;
        }
        if (!shlkm.isHoldingShulker(p, e.getCurrentItem())) {
            return;
        }
        e.setCancelled(true);
    }
}
