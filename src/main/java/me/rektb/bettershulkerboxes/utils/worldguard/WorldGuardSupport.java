package me.rektb.bettershulkerboxes.utils.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WorldGuardSupport {
    public static StateFlag ALLOW_BSB;
    FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
    Runnable supportEnableSuccess;
    Runnable supportEnableFailure;

    public WorldGuardSupport() {
        this(() -> {
        }, () -> {
        });
    }

    public WorldGuardSupport(Runnable success, Runnable fail) {
        this.supportEnableSuccess = success;
        this.supportEnableFailure = fail;
    }


    public void init() {
        try {
            StateFlag flag = new StateFlag("allow-bsb", true);
            registry.register(flag);
            ALLOW_BSB = flag;
            supportEnableSuccess.run();
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("allow-bsb");
            if (existing instanceof StateFlag) {
                ALLOW_BSB = (StateFlag) existing;
            } else {
                supportEnableFailure.run();
            }
        }
    }

    public boolean allowOpen(Player p, Location l) {
        if (l.getWorld() == null) {
            return true;
        }
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        com.sk89q.worldedit.util.Location weLoc = BukkitAdapter.adapt(l);
        ApplicableRegionSet regSet = query.getApplicableRegions(weLoc);
        RegionAssociable associable = WorldGuardPlugin.inst().wrapPlayer(p);
        return regSet.testState(associable, ALLOW_BSB);
    }
}
