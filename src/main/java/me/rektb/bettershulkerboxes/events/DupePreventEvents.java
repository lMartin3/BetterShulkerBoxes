package me.rektb.bettershulkerboxes.events;

import me.rektb.bettershulkerboxes.BetterShulkerBoxes;
import me.rektb.bettershulkerboxes.ConfigurationImport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public class DupePreventEvents implements Listener {
    private BetterShulkerBoxes plugin = BetterShulkerBoxes.getPlugin(BetterShulkerBoxes.class);
    private ConfigurationImport cfgi = plugin.cfgi;

    @EventHandler
    public void dropDupe(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        String nowinvname = p.getOpenInventory().getTitle();
        if ((nowinvname.equalsIgnoreCase(cfgi.invname)) &&
                (e.getItemDrop().getItemStack().toString().contains("SHULKER_BOX"))) {
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
        String nowinvname = p.getOpenInventory().getTitle();
        if ((nowinvname.equalsIgnoreCase(cfgi.invname)) &&
                (p.getInventory().getItemInMainHand().toString().contains("SHULKER_BOX"))) {
            Material holdingshulker = p.getInventory().getItemInMainHand().getType();
            ItemStack itemStack = new ItemStack(holdingshulker);
            BlockStateMeta bsm = (BlockStateMeta) itemStack.getItemMeta();
            ShulkerBox box = (ShulkerBox) bsm.getBlockState();
            box.getInventory().setContents(p.getOpenInventory().getTopInventory().getContents());

            bsm.setBlockState(box);
            box.update();
            itemStack.setItemMeta(bsm);
            p.getInventory().remove(p.getInventory().getItemInMainHand());
            p.getInventory().setItem(p.getInventory().getHeldItemSlot(), itemStack);
            if (cfgi.cfg_closemsg_enabled) {
                p.sendMessage(cfgi.prefix + cfgi.closemsg);
            }
            try {
                p.playSound(p.getLocation(), Sound.valueOf(cfgi.cfg_closesound), 1.0F, 1.0F);
            } catch (Exception el) {
                Bukkit.getServer().getConsoleSender().sendMessage(cfgi.prefix + ChatColor.RED + " <ERROR> Invalid sound: " + ChatColor.YELLOW + cfgi.cfg_closesound);
                p.playSound(p.getLocation(), Sound.BLOCK_SHULKER_BOX_CLOSE, 1.0F, 1.0F);
                p.sendMessage(cfgi.prefix + ChatColor.RED + " <ERROR> Invalid sound: " + ChatColor.YELLOW + cfgi.cfg_closesound + ChatColor.RED + ", please inform an administrator about this.");
            }
        }
    }

    @EventHandler
    public void invalidPrevent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == null) {
            return;
        }
        if (e.getAction().equals(InventoryAction.HOTBAR_SWAP)) {
            e.setCancelled(true);
            return;
        }
        String nowinvname = p.getOpenInventory().getTitle();
        if (p.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            return;
        }
        if (!(nowinvname.equalsIgnoreCase(cfgi.invname))) {
            return;
        }
        if (e.getCurrentItem() == null) {
            return;
        }
        if (e.getCurrentItem().getType().equals(Material.SHULKER_BOX)) {
            e.setCancelled(true);
        }
    }
}
