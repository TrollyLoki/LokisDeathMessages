package net.trollyloki.LokisDeathMessages;

import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Config {
	
	public static String getMessage(LivingEntity entity) {
		
		if (Main.getPlugin().getConfig().getBoolean("mobs.custom.enabled")) {
	    	Collection<String> customMessages = Main.getPlugin().getConfig().getConfigurationSection("mobs.custom.types").getKeys(false);
	    	for (String configType : customMessages) {
	    		
	    		if (entity.getType().name().equalsIgnoreCase(configType)) {
	    			
	    			if (wasKilled(entity)) {
	    				return Main.getString("mobs.custom.types." + configType + ".killed-message");
	    			}
	    			
	    			else {
	    				return Main.getString("mobs.custom.types." + configType + ".died-message");
	    			}
	    			
	    		}
	    		
	    	}
		}
    	
    	
    	if (wasKilled(entity)) {
    		return Main.getString("mobs.killed-message");
    	}
    	
    	else {
    		return Main.getString("mobs.died-message");
    	}
    	
    }
	
	public static String getMessage(Player player) {
		
		if (Main.getPlugin().getConfig().getBoolean("players.custom.enabled")) {
	    	Collection<String> customMessages = Main.getPlugin().getConfig().getConfigurationSection("players.custom.permissions").getKeys(false);
	    	for (String configPerm : customMessages) {
	    		
	    		if (player.hasPermission("deathmsg.custom." + configPerm)) {
	    			
	    			if (wasKilled(player)) {
	    				return Main.getString("players.custom.permissions." + configPerm + ".killed-message");
	    			}
	    			
	    			else {
	    				return Main.getString("players.custom.permissions." + configPerm + ".died-message");
	    			}
	    			
	    		}
	    		
	    	}
		}
    	
    	
    	if (wasKilled(player)) {
    		return Main.getString("players.killed-message");
    	}
    	
    	else {
    		return Main.getString("players.died-message");
    	}
    	
    }
	
	public static boolean wasKilled(LivingEntity victim) {
		
		if (victim.getKiller() != null) return true;
		else if (victim.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
			return true;
		}
		return false;
		
	}
	
	
	public static String killerPlaceholders(String string, LivingEntity victim) {
		Entity killer = null;
		if (victim.getKiller() != null) killer = victim.getKiller();
		else if (victim.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
			killer = ((EntityDamageByEntityEvent) victim.getLastDamageCause()).getDamager();
			if (killer instanceof Projectile && ((Projectile) killer).getShooter() instanceof LivingEntity) {
				killer = (LivingEntity) ((Projectile) killer).getShooter();
			}
			else if (killer instanceof AreaEffectCloud && ((AreaEffectCloud) killer).getSource() instanceof LivingEntity) {
				killer = (LivingEntity) ((AreaEffectCloud) killer).getSource();
			}
		}
		else return string;
		
		if (killer instanceof Player) {
			Team team = getScoreboardTeam((Player)killer, Bukkit.getScoreboardManager().getMainScoreboard());
			string = string
			.replaceAll("%killer displayname%", ((Player)killer).getDisplayName())
			.replaceAll("%killer level%", String.valueOf(((Player)killer).getLevel()))
			.replaceAll("%victim prefix%", team != null ? team.getPrefix() : "")
			.replaceAll("%victim suffix%", team != null ? team.getSuffix() : "");
		}
		else string = string
		.replaceAll("%killer displayname%", killer.getName())
		.replaceAll("%killer level%", "-")
		.replaceAll("%victim prefix%", "")
		.replaceAll("%victim suffix%", "");
		
		return string
		.replaceAll("%killer name%", killer.getType() == EntityType.FALLING_BLOCK ?
				underscoreToSpace(((FallingBlock)killer).getBlockData().getMaterial().toString()) : killer.getName())
		.replaceAll("%killer uuid%", killer.getUniqueId().toString())
		.replaceAll("%killer entityid%", String.valueOf(killer.getEntityId())
		.replaceAll("%killer location%", Config.getCoords(killer))
		.replaceAll("%killer world%", killer.getWorld().getName()));
	}
	
	public static String victimPlaceholders(String string, Player player) {
		string = victimPlaceholders(string, (LivingEntity) player);
		Team team = getScoreboardTeam(player, Bukkit.getScoreboardManager().getMainScoreboard());
		return string
		.replaceAll("%victim displayname%", player.getDisplayName())
		.replaceAll("%victim level%", String.valueOf(player.getLevel()))
		.replaceAll("%victim prefix%", team != null ? team.getPrefix() : "")
		.replaceAll("%victim suffix%", team != null ? team.getSuffix() : "");
	}
	
	public static String victimPlaceholders(String string, LivingEntity mob) {
		return string
		.replaceAll("%victim name%", mob.getName())
		.replaceAll("%victim type%", Config.underscoreToSpace(mob.getType().name()))
		.replaceAll("%victim uuid%", mob.getUniqueId().toString())
		.replaceAll("%victim entityid%", String.valueOf(mob.getEntityId()))
		.replaceAll("%victim location%", Config.getCoords(mob))
		.replaceAll("%victim world%", mob.getWorld().getName());
	}
	
	
	public static String getCause(LivingEntity mob) {
		String cause = mob.getLastDamageCause().getCause().name();
		if (mob.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
			Entity damager = ((EntityDamageByEntityEvent) mob.getLastDamageCause()).getDamager();
			if (damager instanceof AreaEffectCloud) {
				cause = DamageCause.MAGIC.name();
				if (((AreaEffectCloud) damager).getSource() instanceof EnderDragon) cause = DamageCause.DRAGON_BREATH.name();
			}
		}
		
		String path = "causes.died." + cause;
		if (wasKilled(mob)) path = "causes.killed." + cause;
		String custom = Main.getString(path);
		
		if (custom != null) {
			return custom;
		}
		
		else {
			return "died";
		}
	}
	
	public static Team getScoreboardTeam(Player player, Scoreboard sb) {
		return sb.getTeam(player.getName());
	}
	
	public static String getCoords(Entity entity) {
		Location loc = entity.getLocation();
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		String coords = (x + ", " + y + ", " + z);
		return coords;
	}
	
	
	
	public static String capFirst(String string) {
    	StringBuilder sb = new StringBuilder();
    	
		for (char c : string.toCharArray()) {
			sb.append(c);
		}
		
		char firstLetter = sb.charAt(0);
		String typeLetter = String.valueOf(firstLetter);
		sb.replace(0, 1, typeLetter.toUpperCase());
		
		String finalString = sb.toString();
		return finalString;
    }
    
    public static String underscoreToSpace(String string) {
    	String[] words = string.split("_");
    	if (words.length < 1) return null;
    	if (words.length < 2) return capFirst(string.toLowerCase());
    	String sepString = "";
    	for (String word : words) {
    		sepString += capFirst(word) + " ";
    	}
    	
    	StringBuilder sb = new StringBuilder();
    	for (char c: sepString.toCharArray()) {
    		sb.append(c);
    	}
    	sb.deleteCharAt(sb.length() - 1);
    	
    	return sb.toString();
    }
	
}