package net.trollyloki.LokisDeathMessages.update;

import java.io.File;

import net.trollyloki.LokisDeathMessages.Main;

public class ConfigUpdater {
	
	public static void updateConfig() {
    	if (Main.getPlugin().getDescription().getVersion().equalsIgnoreCase("1.0")) {
    		if (Main.getPlugin().getConfig().getString("do-not-touch_version").equalsIgnoreCase("1.0")) {
    			Main.getPlugin().getLogger().info("The config format is up to date");
    		}
    		
    		else {
    			Main.getPlugin().getLogger().warning("No valid version found in the config. Unable to run format check");
    		}
    	}
    }
	
	public static void resetConfig() {
    	Main.getPlugin().reloadConfig();
    	File config = new File(Main.getPlugin().getDataFolder(), "config.yml");
    	config.delete();
    	Main.getPlugin().saveDefaultConfig();
	}
}