package me.rektb.bettershulkerboxes;

import me.rektb.bettershulkerboxes.commands.MainCommand;
import me.rektb.bettershulkerboxes.events.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BetterShulkerBoxes extends JavaPlugin implements Listener {

    //instance given to ConfigurationImport
    public BetterShulkerBoxes cfginst;
    public MainCommand maincmd;

    public InteractEvent interactEvent;
    public InvClickEvent invClickEvent;
    public InvCloseEvent invCloseEvent;
    public DupePreventEvents dupePreventEvents;
    public PlyrJoinEvent plyrJoinEvent;

    public boolean updatefound = false;
    public String lastver = "";
    public String resourceurl = "";
    public UpdateChecker updater = new UpdateChecker(this, 58837);
    public ConfigurationImport cfgi;
    public ShulkerManage shlkm;

    public void onEnable() {
        final String regex = "([0-9]{1,2}\\.[0-9]{1,2}(\\.[0-9]{1,2})?)"; //This should get the MC version from "git-Spigot-2ee05fe-d31f05f (MC: 1.15.1)"
        final Matcher m = Pattern.compile(regex).matcher(getServer().getVersion());
        final List<String> matches = new ArrayList<>();
        while (m.find()) {
            matches.add(m.group(0));
        }
        String version = "invalid";
        if (matches.size() > 0) {
            version = matches.get(0);
        }
        getServer().getConsoleSender().sendMessage(String.valueOf(version.split("\\.")[1]));
        if (!version.equals("invalid") && Integer.parseInt(version.split("\\.")[0]) < 13) { // Just a warn when used in versions under 1.13
            getServer().getConsoleSender().sendMessage(ChatColor.RED + String.format("Warning! BetterShulkerBoxes does" +
                    " NOT support %s officially, if you find any problems contact the developer. I am not responsible" +
                    " for players duping items and/or thermonuclear war.", version));
        }
        getServer().getConsoleSender().sendMessage(matches.get(0));

        loadConfig();
        checkConfigValidity();

        shlkm = new ShulkerManage();
        interactEvent = new InteractEvent();
        invClickEvent = new InvClickEvent(this);
        invCloseEvent = new InvCloseEvent();
        dupePreventEvents = new DupePreventEvents();
        plyrJoinEvent = new PlyrJoinEvent();
        maincmd = new MainCommand();

        getCommand(maincmd.command).setExecutor(new MainCommand());
        getServer().getPluginManager().registerEvents(interactEvent, this);
        getServer().getPluginManager().registerEvents(invClickEvent, this);
        getServer().getPluginManager().registerEvents(invCloseEvent, this);
        getServer().getPluginManager().registerEvents(dupePreventEvents, this);
        getServer().getPluginManager().registerEvents(plyrJoinEvent, this);
        getServer().getPluginManager().registerEvents(plyrJoinEvent, this);

        if (cfgi.cfg_statistics) {
            Metrics metrics = new Metrics(this);
        } else {
            getServer().getConsoleSender().sendMessage("Statistics have been disabled, please consider enabling them to" +
                    "help plugin development.");
        }
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Better Shulkerboxes enabled - Plugin written by Rektb");

        Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {
            public void run() {
                BetterShulkerBoxes.this.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "BSB is checking for updates...");
                try {
                    if (BetterShulkerBoxes.this.updater.checkForUpdates()) {
                        updater.getChangelog();
                        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Update found! You're using " + getDescription().getVersion() + " New version: " + BetterShulkerBoxes.this.updater.getLatestVersion() + ", download at: " + BetterShulkerBoxes.this.updater.getResourceURL());
                        BetterShulkerBoxes.this.updatefound = true;
                        BetterShulkerBoxes.this.lastver = BetterShulkerBoxes.this.updater.getLatestVersion();
                        BetterShulkerBoxes.this.resourceurl = BetterShulkerBoxes.this.updater.getResourceURL();
                    } else {
                        BetterShulkerBoxes.this.getLogger().info(ChatColor.GREEN + "Better Shulker Boxes is up to date!");
                    }
                } catch (Exception e) {
                    BetterShulkerBoxes.this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Error! BSB could not check for updates:");
                    e.printStackTrace();
                }
            }
        });
    }

    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "Better Shulkerboxes disabled");
    }

    public void checkConfigValidity() {
        cfginst = this;
        cfgi = new ConfigurationImport();
        if (!cfgi.checkConfigurationValidity().isEmpty()) {
            throwConfigurationErrror(cfgi.checkConfigurationValidity());
        }
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }


    public void throwConfigurationErrror(String error) {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "<ERROR> BetterShulkerBoxes found an invalid configuration file. Please fix the following issue(s):");
        for (String s : error.split("-")) {
            getServer().getConsoleSender().sendMessage("-> " + s);
        }
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "Disabling plugin");
        Bukkit.getPluginManager().disablePlugin(this);
    }

    public void newInstances() {
        this.maincmd.getNewInstances();
        this.dupePreventEvents.getNewInstances();
        this.interactEvent.getNewInstances();
        this.invCloseEvent.getNewInstances();
        this.shlkm.getNewInstances();
    }

}
