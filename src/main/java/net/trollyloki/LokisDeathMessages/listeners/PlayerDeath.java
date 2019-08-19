package net.trollyloki.LokisDeathMessages.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.trollyloki.LokisDeathMessages.Config;
import net.trollyloki.LokisDeathMessages.Main;

public class PlayerDeath implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDeath(PlayerDeathEvent event) {
		
		if (!Main.getPlugin().getConfig().getBoolean("players.enabled")) return;
		
		Player player = event.getEntity();
		String message = Config.getMessage(player);
		
		message = Config.killerPlaceholders(message, player);
		
		message = Config.victimPlaceholders(message, player);
		message = message.replaceAll("%cause%", Config.getCause(player));
		
		event.setDeathMessage(message);
		
    }
	
}