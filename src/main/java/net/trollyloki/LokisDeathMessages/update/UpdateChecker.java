package net.trollyloki.LokisDeathMessages.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.trollyloki.LokisDeathMessages.Main;

public class UpdateChecker implements Listener {
	
	public static boolean updateAvailable = false;
	public static String newVersion;
	
	private static String resourceId = "55023";
	private static URL updateUrl;
	private static String resourceUrl;
	
	public static void run() {
		
		resourceUrl = "https://spigotmc.org/resources/lokis-death-messages." + resourceId + "/";
		
		if (Main.getPlugin().getConfig().getBoolean("update-checker") == false) {
			return;
		}
		
		try {
			updateUrl = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				try {
					
					URLConnection con = updateUrl.openConnection();
			        newVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
			        updateAvailable = !(Main.getPlugin().getDescription().getVersion().equals(newVersion));
			        
				} catch (IOException e) {
					Main.getPlugin().getLogger().warning("Failed to check for updates");
				}
				
			}
		});
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				if (updateAvailable) {
					Bukkit.getConsoleSender().sendMessage(getMsg());
				}
			}
		}, 40L);
		
	}
	
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();
		if (player.hasPermission("deathmsg.update")) {
			
			if (updateAvailable) {
				player.sendMessage(getMsg());
			}
			
		}
		
	}
	
	private static String getMsg() {
		
		return Main.prefix() + Main.getString("lang.update")
		.replaceAll("%current%", Main.getPlugin().getDescription().getVersion())
		.replaceAll("%new%", newVersion)
		.replaceAll("%url%", resourceUrl);
		
	}
	
}