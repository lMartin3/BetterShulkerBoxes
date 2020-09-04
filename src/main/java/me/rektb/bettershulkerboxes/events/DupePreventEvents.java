package me.rektb.bettershulkerboxes.events;

import me.rektb.bettershulkerboxes.BetterShulkerBoxes;
import me.rektb.bettershulkerboxes.utils.ConfigurationImport;
import me.rektb.bettershulkerboxes.utils.ShulkerManage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.inventory.ItemStack;


public class DupePreventEvents implements Listener {
    private BetterShulkerBoxes plugin = BetterShulkerBoxes.getPlugin(BetterShulkerBoxes.class);
    private ConfigurationImport cfgi = plugin.cfgi;
    private final ShulkerManage shlkm = plugin.shlkm;

    // This class prevents some dupes that could occur

    // Player opens shulkerbox -> grabs items -> swaps it with another slot in the hotbar with numkeys -> closes inv -> contents not saved -> dupe
    @EventHandler
    public void swapDupe(InventoryClickEvent e) {
        if (!e.getClick().equals(ClickType.NUMBER_KEY)) {
            return;
        }
        if (!e.getAction().equals(InventoryAction.HOTBAR_SWAP)) {
            return;
        }
        Player p = (Player) e.getWhoClicked();
        if (p.getInventory().getItem(e.getHotbarButton()) == null) {
            return;
        }
        if (!shlkm.isHoldingShulker(p.getInventory().getItemInMainHand())) {
            return;
        }
        if (!shlkm.isInventoryShulker(p.getInventory().getItem(e.getHotbarButton()), p.getOpenInventory().getTitle())) {
            return;
        }
        if (!p.getInventory().getItemInMainHand().equals(p.getInventory().getItem(e.getHotbarButton()))) {
            return;
        }
        e.setCancelled(true);
    }

    // Player opens shulkerbox -> grabs items -> drops it -> contents were not saved -> dupe
    @EventHandler
    public void dropDupe(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        ItemStack idr = e.getItemDrop().getItemStack();
        if (shlkm.isInventoryShulker(idr, p.getOpenInventory().getTitle()) && (e.getItemDrop().getItemStack().toString().contains("SHULKER_BOX"))) {
            e.setCancelled(true);
        }
    }

    // Player opens shulkerbox -> withdraws items from it -> commits suicide -> invCloseEvent was not called and content's didn't save -> dupe
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

    // Player opens shulkerbox -> before gui opens changes it to off hand -> the shulker inventory is untracked -> dupe
    @EventHandler
    public void offHandDupe(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        boolean isinvshulker = shlkm.isInventoryShulker(p.getInventory().getItemInOffHand(), e.getView().getTitle());
        
        if (isinvshulker && shlkm.isHoldingShulker(p.getInventory().getItemInOffHand())) {
            p.sendMessage(cfgi.prefix + cfgi.offhandmsg);
            e.setCancelled(true);
            return;
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
        if (!shlkm.isHoldingShulker(e.getCurrentItem())) {
            return;
        }

        e.setCancelled(true);
    }

    // yes
    public void getNewInstances() {
        this.plugin = BetterShulkerBoxes.getPlugin(BetterShulkerBoxes.class);
        this.cfgi = plugin.cfgi;
    }

    //This only occurs in OLD versions of paper, as the player inventory isn't closed when going through a portal.
    //This was added only so those who like waiting 20s before the server starts don't experience the bug.
    @EventHandler
    public void onDimensionChange(PlayerPortalEvent e) {
        Player p = e.getPlayer();
        if (shlkm.isInventoryShulker(p.getInventory().getItemInMainHand(), p.getOpenInventory().getTitle()) && (p.getInventory().getItemInMainHand().toString().contains("SHULKER_BOX"))) {
            shlkm.closeShulker(p, p.getInventory().getItemInMainHand(), p.getOpenInventory().getTopInventory());
        }
    }
}
