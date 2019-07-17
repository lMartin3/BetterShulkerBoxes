package me.rektb.bettershulkerboxes.events;

import me.rektb.bettershulkerboxes.BetterShulkerBoxes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.plugin.Plugin;

public class InvCloseEvent
        implements Listener {
    private Plugin plugin = BetterShulkerBoxes.getPlugin(BetterShulkerBoxes.class);
    private final String cfg_prefix = this.plugin.getConfig().getString("prefix");
    private String prefix = ChatColor.translateAlternateColorCodes('&', this.cfg_prefix);
    private final String cfg_closemsg = this.plugin.getConfig().getString("close_msg");
    private String closemsg = ChatColor.translateAlternateColorCodes('&', this.cfg_closemsg);
    private final boolean cfg_closemsg_enabled = this.plugin.getConfig().getBoolean("close_msg_enabled");
    private final String cfg_closesound = this.plugin.getConfig().getString("close_sound");
    private final String cfg_invname = this.plugin.getConfig().getString("inventory_name");
    private String invname = ChatColor.translateAlternateColorCodes('&', this.cfg_invname);

    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        String nowinvname = e.getView().getTitle();
        if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.SHULKER_BOX)) {
            Material holdingshulker = e.getPlayer().getInventory().getItemInMainHand().getType();
            if (nowinvname.equalsIgnoreCase(this.invname)) {
                String getitemname = e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName();
                ItemStack itemStack = new ItemStack(holdingshulker);

                BlockStateMeta bsm = (BlockStateMeta) itemStack.getItemMeta();
                ShulkerBox box = (ShulkerBox) bsm.getBlockState();
                box.getInventory().setContents(e.getInventory().getContents());
                if (getitemname != "") {
                    bsm.setDisplayName(getitemname);
                }
                bsm.setBlockState(box);
                box.update();
                itemStack.setItemMeta(bsm);
                p.getInventory().setItem(p.getInventory().getHeldItemSlot(), itemStack);
                if (this.cfg_closemsg_enabled) {
                    p.sendMessage(this.prefix + this.closemsg);
                }
                try {
                    p.playSound(p.getLocation(), Sound.valueOf(this.cfg_closesound), 1.0F, 1.0F);
                } catch (Exception el) {
                    Bukkit.getServer().getConsoleSender().sendMessage(this.prefix + ChatColor.RED + " <ERROR> Invalid sound: " + ChatColor.YELLOW + this.cfg_closesound);
                    p.playSound(p.getLocation(), Sound.BLOCK_SHULKER_BOX_CLOSE, 1.0F, 1.0F);
                    p.sendMessage(this.prefix + ChatColor.RED + " <ERROR> Invalid sound: " + ChatColor.YELLOW + this.cfg_closesound + ChatColor.RED + ", please inform an administrator about this.");
                }
            }
        }
    }
}
