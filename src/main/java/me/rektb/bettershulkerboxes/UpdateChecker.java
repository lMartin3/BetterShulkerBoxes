package me.rektb.bettershulkerboxes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class UpdateChecker {
    private int project;
    private URL checkURL;
    private URL changelogURL;
    private String newVersion;
    private JavaPlugin plugin;

    UpdateChecker(JavaPlugin plugin, int projectID) {
        this.plugin = plugin;
        this.newVersion = plugin.getDescription().getVersion();
        this.project = projectID;
        try {
            this.checkURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + projectID);
        } catch (MalformedURLException localMalformedURLException) {
            Bukkit.getServer().getConsoleSender().sendMessage("Error: MalformedURLException, please send this to the developer");
        }
        try {
            this.changelogURL = new URL("https://raw.githubusercontent.com/lMartin3/BetterShulkerBoxes/master/CHANGELOG.txt");
        } catch (MalformedURLException localMalformedURLException) {
            Bukkit.getServer().getConsoleSender().sendMessage("Error: MalformedURLException, please send this to the developer");
        }

    }

    public int getProjectID() {
        return this.project;
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    public String getLatestVersion() {
        return this.newVersion;
    }

    public String getResourceURL() {
        return "https://www.spigotmc.org/resources/" + this.project;
    }

    public String getActualVersion() {
        return this.plugin.getDescription().getVersion();
    }

    public boolean checkForUpdates() {
        try {
            URLConnection con = this.checkURL.openConnection();
            this.newVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            return !this.plugin.getDescription().getVersion().equals(this.newVersion);
        } catch (IOException ioex) {
            plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Error! BSB could not check for updates:");
            plugin.getServer().getConsoleSender().sendMessage(ioex.toString());
            return false;
        }
    }

    public ArrayList<String> getChangelog() {
        ArrayList<String> lines = new ArrayList<>();
        try {
            URLConnection con = this.changelogURL.openConnection();
            InputStreamReader inSR = new InputStreamReader(con.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inSR);
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = bufferedReader.readLine()) != null) {
                lines.add(inputLine);
            }
            bufferedReader.close();
            return lines;
        } catch (IOException ioex) {
            plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Error! BSB could not retrieve the changelog:");
            plugin.getServer().getConsoleSender().sendMessage(ioex.toString());
            return lines;
        }
    }

}
