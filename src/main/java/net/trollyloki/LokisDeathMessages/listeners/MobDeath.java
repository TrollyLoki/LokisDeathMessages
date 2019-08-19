package net.trollyloki.LokisDeathMessages.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import net.trollyloki.LokisDeathMessages.Config;
import net.trollyloki.LokisDeathMessages.Main;

public class MobDeath implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMobDeath(EntityDeathEvent event) {
		if (!Main.getPlugin().getConfig().getBoolean("mobs.enabled")) return;
		
    	if (event.getEntityType() != EntityType.PLAYER) {
    		
    		LivingEntity mob = event.getEntity();
    		String message = Config.getMessage(mob);
    		
			message = Config.killerPlaceholders(message, mob);
    		
    		message = Config.victimPlaceholders(message, mob);
    		message = message.replaceAll("%cause%", Config.getCause(mob));
    		
    		Main.broadcastMessages(message);
    		
    	}
    }
	
}