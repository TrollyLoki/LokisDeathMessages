package net.trollyloki.LokisDeathMessages;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.trollyloki.LokisDeathMessages.commands.DeathMessageCommand;
import net.trollyloki.LokisDeathMessages.listeners.MobDeath;
import net.trollyloki.LokisDeathMessages.listeners.PlayerDeath;
import net.trollyloki.LokisDeathMessages.update.ConfigUpdater;
import net.trollyloki.LokisDeathMessages.update.UpdateChecker;

public class Main extends JavaPlugin implements Listener {
	
	public static JavaPlugin plugin;
	
    @Override
    public void onEnable() {
    	plugin = this;
    	registerEvents(this, new PlayerDeath(), new MobDeath());
    	getCommand("deathmsg").setExecutor(new DeathMessageCommand());
    	saveDefaultConfig();
    	ConfigUpdater.updateConfig();
    	UpdateChecker.run();
    	
    }
    
    @Override
    public void onDisable() {
    	plugin = null;
    }
    
    public static void registerEvents(Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }
    
    public static Plugin getPlugin() {
        return plugin;
    }
    
    /*public void runUpdateChecker() {
    	updateChecker.checkForUpdates();
    	if (updateChecker.updateStatus != null) {
    		getPlugin().getLogger().info(updateChecker.updateStatus);
    	}
    }*/
    
    public static String getString(String path) {
    	String translation = plugin.getConfig().getString(path);
    	if (translation != null) {
    		translation = ChatColor.translateAlternateColorCodes('&', translation);
    		return translation;
    	}
    	else {
    		return (ChatColor.GRAY + "" + ChatColor.ITALIC + path + ChatColor.RESET);
    	}
    }
    
    public static String prefix() {
    	return getString("lang.prefix");
    }
    
    
    
    public static void colorConsole(String message) {
    	ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
    	console.sendMessage(message);
    }
    
    public static void broadcastMessages(String... messages) {
    	for (String message : messages) {
    		colorConsole(message);
    		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
    			player.sendMessage(message);
    		}
    	}
    }
}
